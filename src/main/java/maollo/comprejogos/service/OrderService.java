package maollo.comprejogos.service;

import maollo.comprejogos.domain.Order;
import maollo.comprejogos.domain.UserCompreJogos;
import maollo.comprejogos.dto.OrderResponseDTO;
import maollo.comprejogos.dto.PaymentInitiationDTO;

import java.util.List;

public interface OrderService {

    PaymentInitiationDTO initiateCheckout(String userEmail, String paymentMethod);

    // Novo método para lidar com a confirmação (será chamado pelo webhook)
    void handlePaymentConfirmation(Long orderId, String status);

    Order createOrder(UserCompreJogos user, List<Long> gameAppIds) throws Exception;

    List<OrderResponseDTO> findOrdersByUser(String userEmail);

}