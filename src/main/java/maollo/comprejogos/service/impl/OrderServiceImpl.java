package maollo.comprejogos.service.impl;

import lombok.RequiredArgsConstructor;
import maollo.comprejogos.domain.*;
import maollo.comprejogos.dto.OrderResponseDTO;
import maollo.comprejogos.exception.ResourceNotFoundException;
import maollo.comprejogos.mapper.OrderMapper;
import maollo.comprejogos.repository.*;
import maollo.comprejogos.service.OrderService;
import maollo.comprejogos.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GameRepository gameRepository;
    private final CartRepository cartRepository;
    private final UserCompreJogosRepository userRepository;
    private final UserGameRepository userGameRepository;
    private final OrderMapper orderMapper;




    @Override
    @Transactional // Garante que tudo (criar pedido, criar UserGames, limpar carrinho) funcione ou nada seja salvo
    public OrderResponseDTO checkout(String userEmail) {
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o usuário: " + userEmail));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio. Não é possível finalizar a compra.");
        }

        // 1. Criar o Pedido
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.APROVADO); // Simulando pagamento aprovado
        order.setTotal(cart.getTotal());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setGame(cartItem.getGame());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);

            // 2. Adicionar Jogo à Biblioteca do Usuário
            // Verifica se o usuário já possui o jogo (caso de re-compra, embora raro)
            if (userGameRepository.findByUserAndGame(user, cartItem.getGame()).isEmpty()) {
                UserGame userGame = new UserGame();
                userGame.setUser(user);
                userGame.setGame(cartItem.getGame());
                userGame.setPurchasePrice(cartItem.getPrice());
                userGameRepository.save(userGame);
            }
        }
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 3. Limpar o Carrinho
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }


    @Override
    @Transactional // Garante que ou tudo funciona, ou nada é salvo no banco (atomicidade)
    public Order createOrder(UserCompreJogos user, List<Long> gameAppIds) throws Exception {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDENTE);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Long appId : gameAppIds) {
            // Busca o jogo no banco de dados
            Game game = gameRepository.findByAppId(appId)
                    .orElseThrow(() -> new Exception("Jogo com AppId " + appId + " não encontrado."));

            // Cria o item do pedido
            OrderItem item = new OrderItem();
            item.setGame(game);
            // Lógica de preço: usa o promocional se estiver ativo, senão o preço normal
            BigDecimal price = game.isPromotionActive() && game.getPromotionalPrice() != null
                    ? game.getPromotionalPrice()
                    : game.getPrice();
            item.setPrice(price);
            item.setQuantity(1); // Simplificando para 1 unidade por jogo
            item.setOrder(order); // Associa o item ao pedido

            orderItems.add(item);
            total = total.add(price); // Soma o preço ao total do pedido
        }

        order.setItems(orderItems);
        order.setTotal(total);

        // Aqui você adicionaria a lógica de pagamento, validação de saldo do usuário, etc.

        order.setStatus(OrderStatus.APROVADO); // Simulando pagamento aprovado
        return orderRepository.save(order);
    }

    @Override
    public List<OrderResponseDTO> findOrdersByUser(String userEmail) {
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(orderMapper::toOrderResponseDTO)
                .collect(Collectors.toList());
    }
}