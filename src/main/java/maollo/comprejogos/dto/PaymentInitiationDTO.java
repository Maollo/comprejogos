package maollo.comprejogos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationDTO {
    private Integer orderId; // ID do nosso pedido interno
    private String paymentGatewayReference; // ID do gateway
    private String paymentMethod; // Método escolhido
    private String paymentDetails; // Link de pagamento, QR code data, etc.
}