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
import maollo.comprejogos.dto.PaymentInitiationDTO; // Importe o DTO
import java.time.LocalDateTime; // Importe LocalDateTime
import org.slf4j.Logger; // Importe Logger
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GameRepository gameRepository;
    private final CartRepository cartRepository;
    private final UserCompreJogosRepository userRepository;
    private final UserGameRepository userGameRepository;
    private final OrderMapper orderMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    @Transactional // A transação agora é mais curta
    public PaymentInitiationDTO initiateCheckout(String userEmail, String paymentMethod) {
        UserCompreJogos user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + userEmail));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para o usuário: " + userEmail));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        // 1. Criar o Pedido como PENDENTE
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDENTE); // <- MUDANÇA IMPORTANTE
        order.setTotal(cart.getTotal());
        order.setPaymentMethod(paymentMethod); // Armazena o método escolhido

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setGame(cartItem.getGame());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);

        // *** AQUI ENTRARIA A CHAMADA PARA A API DO GATEWAY DE PAGAMENTO ***
        // Exemplo conceitual (você substituirá isso pela API real):
        // PaymentGatewayResponse gatewayResponse = paymentGatewayClient.createPayment(order.getTotal(), paymentMethod, order.getId());
        // String gatewayReference = gatewayResponse.getReferenceId();
        // String paymentLink = gatewayResponse.getPaymentUrl();

        // Simulando a resposta do gateway:
        String gatewayReference = "MOCK_PAYMENT_REF_" + System.currentTimeMillis(); // Gere um ID único
        String paymentLink = "https://mock-payment-gateway.com/pay/" + gatewayReference; // Link simulado

        // Atualiza o pedido com os detalhes do gateway
        order.setPaymentGatewayReference(gatewayReference);
        order.setPaymentDetails(paymentLink); // Ou QR Code data, etc.

        Order savedOrder = orderRepository.save(order);
        logger.info("Pedido {} criado com status PENDENTE para usuário {}. Ref Gateway: {}",
                savedOrder.getId(), userEmail, gatewayReference);

        // !! NÃO ADICIONA À BIBLIOTECA E NÃO LIMPA O CARRINHO AINDA !!

        // Retorna os detalhes para o frontend iniciar o pagamento
        return new PaymentInitiationDTO(
                savedOrder.getId(),
                savedOrder.getPaymentGatewayReference(),
                savedOrder.getPaymentMethod(),
                savedOrder.getPaymentDetails()
        );
    }

    @Override
    @Transactional // ESSENCIAL que esta operação seja atômica
    public void handlePaymentConfirmation(String paymentGatewayReference, String gatewayStatus) {
        logger.info("Recebida notificação do gateway. Referência: {}, Status: {}", paymentGatewayReference, gatewayStatus);

        // Busca o pedido pela referência ÚNICA do gateway
        Optional<Order> orderOpt = orderRepository.findByPaymentGatewayReference(paymentGatewayReference); // PRECISA CRIAR ESTE MÉTODO NO REPOSITORY

        if (orderOpt.isEmpty()) {
            logger.error("ERRO: Pedido não encontrado para a referência do gateway: {}", paymentGatewayReference);
            // Poderia lançar uma exceção ou apenas retornar
            return;
        }

        Order order = orderOpt.get();

        // Idempotência: Se o pedido já foi processado (APROVADO ou CANCELADO), não faz nada.
        if (order.getStatus() != OrderStatus.PENDENTE) {
            logger.warn("Pedido {} já processado. Status atual: {}. Ignorando notificação.", order.getId(), order.getStatus());
            return;
        }

        // Verifica o status recebido do gateway
        if ("APROVADO".equalsIgnoreCase(gatewayStatus)) { // Adapte para o status real do seu gateway
            logger.info("Confirmando pagamento para o pedido {}", order.getId());
            order.setStatus(OrderStatus.APROVADO);
            order.setPaymentConfirmedAt(LocalDateTime.now());

            // 1. Adiciona os jogos à biblioteca do usuário
            for (OrderItem item : order.getItems()) {
                // Verifica novamente se o usuário já possui (segurança extra)
                if (userGameRepository.findByUserAndGame(order.getUser(), item.getGame()).isEmpty()) {
                    UserGame userGame = new UserGame();
                    userGame.setUser(order.getUser());
                    userGame.setGame(item.getGame());
                    userGame.setPurchasePrice(item.getPrice());
                    userGameRepository.save(userGame);
                    logger.info("Jogo {} adicionado à biblioteca do usuário {}", item.getGame().getAppId(), order.getUser().getEmail());
                } else {
                    logger.warn("Usuário {} já possui o jogo {}. Não adicionando novamente.", order.getUser().getEmail(), item.getGame().getAppId());
                }
            }

            // 2. Limpa o carrinho do usuário
            cartRepository.findByUser(order.getUser()).ifPresent(cart -> {
                if (!cart.getItems().isEmpty()) {
                    cart.getItems().clear();
                    cartRepository.save(cart);
                    logger.info("Carrinho do usuário {} limpo após confirmação do pedido {}.", order.getUser().getEmail(), order.getId());
                }
            });

        } else if ("CANCELADO".equalsIgnoreCase(gatewayStatus) || "REJEITADO".equalsIgnoreCase(gatewayStatus)) { // Adapte
            logger.warn("Pagamento para o pedido {} foi cancelado/rejeitado. Status: {}", order.getId(), gatewayStatus);
            order.setStatus(OrderStatus.CANCELADO);
        } else {
            logger.error("Status desconhecido recebido do gateway para o pedido {}: {}", order.getId(), gatewayStatus);
            // Talvez definir um status de ERRO? Ou manter PENDENTE?
            // Por segurança, vamos manter PENDENTE e investigar manualmente.
        }

        orderRepository.save(order); // Salva as alterações no pedido (status, data de confirmação)
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

    @Override
    public void simulateSuccessfulPayment(String paymentGatewayReference) {
        logger.info("SIMULAÇÃO: Iniciando confirmação de pagamento para referência {}", paymentGatewayReference);
        // Chama a mesma lógica que o webhook usaria
        handlePaymentConfirmation(paymentGatewayReference, "APROVADO");
        logger.info("SIMULAÇÃO: Confirmação de pagamento concluída para referência {}", paymentGatewayReference);
    }
}