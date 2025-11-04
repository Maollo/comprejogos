package maollo.comprejogos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maollo.comprejogos.dto.OrderRequestDTO;
import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.OrderResponseDTO;
import maollo.comprejogos.dto.SimulatePaymentRequestDTO;
import maollo.comprejogos.service.OrderService;
import maollo.comprejogos.service.UserCompreJogosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import maollo.comprejogos.dto.PaymentInitiationDTO;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserCompreJogosService userService;


    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentInitiationDTO> initiateCheckout( // 3. RETORNO MUDA PARA PaymentInitiationDTO
                                                                  @AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam String paymentMethod // 4. RECEBE O MÉTODO DE PAGAMENTO
    ) {
        // 5. CHAMA O NOVO MÉTODO DO SERVIÇO
        PaymentInitiationDTO paymentInfo = orderService.initiateCheckout(userDetails.getUsername(), paymentMethod);

        // Retorna 201 Created com os detalhes de iniciação do pagamento
        return new ResponseEntity<>(paymentInfo, HttpStatus.CREATED);
    }

    /**
     * Endpoint para o usuário ver seu histórico de pedidos.
     */
    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderResponseDTO> orders = orderService.findOrdersByUser(userDetails.getUsername());
        return ResponseEntity.ok(orders);
    }
    /**
     * Endpoint para criar um novo pedido.
     * HTTP POST para /api/orders
     * @param orderRequest DTO contendo o ID do usuário e a lista de App IDs dos jogos.
     * @return O pedido criado com status 201 (Created) ou uma mensagem de erro.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        // IMPORTANTE: Em uma aplicação real, o usuário viria de um contexto de segurança (login),
        // não do corpo da requisição. Estamos simplificando por enquanto.
        Optional<UserCompreJogos> userOptional = userService.findById(orderRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        try {
            Order newOrder = orderService.createOrder(userOptional.get(), orderRequest.getGameAppIds());
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para buscar o histórico de pedidos de um usuário.
     * HTTP GET para /api/orders/user/{userId}
     * @param userId O ID do usuário.
     * @return A lista de pedidos do usuário.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
        Optional<UserCompreJogos> userOptional = userService.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        List<OrderResponseDTO> orders = orderService.findOrdersByUser(userOptional.get().getUsername());
        return ResponseEntity.ok(orders);
    }

}