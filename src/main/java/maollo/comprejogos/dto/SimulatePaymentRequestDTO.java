package maollo.comprejogos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SimulatePaymentRequestDTO {
    @NotBlank(message = "A referência do pagamento é obrigatória")
    private String paymentGatewayReference;
}