package maollo.comprejogos.service.impl;

import maollo.comprejogos.domain.*;
import maollo.comprejogos.repository.GameRepository;
import maollo.comprejogos.repository.OrderRepository;
import maollo.comprejogos.service.OrderService;
import maollo.comprejogos.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GameRepository gameRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, GameRepository gameRepository) {
        this.orderRepository = orderRepository;
        this.gameRepository = gameRepository;
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
    public List<Order> findOrdersByUser(UserCompreJogos user) {
        return orderRepository.findByUser(user);
    }
}