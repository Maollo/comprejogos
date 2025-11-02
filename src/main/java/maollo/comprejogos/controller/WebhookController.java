package maollo.comprejogos.controller;

import maollo.comprejogos.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // Para receber dados genéricos do webhook

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private OrderService orderService;

    // Exemplo para Mercado Pago (a estrutura do payload varia por gateway)
    @PostMapping("/mercado-pago/payment")
    public ResponseEntity<Void> handleMercadoPagoWebhook(@RequestBody Map<String, Object> payload) {
        logger.info("Recebido webhook do Mercado Pago: {}", payload);

        try {
            // Extraia a referência do pagamento e o status do payload
            // A estrutura exata depende da API do Mercado Pago
            // Exemplo HIPOTÉTICO:
            String action = (String) payload.get("action");
            if ("payment.updated".equals(action)) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String paymentId = (String) data.get("id"); // Este seria o paymentGatewayReference? VERIFICAR DOCS

                // Você precisaria chamar a API do MP para pegar o status real do pagamento 'paymentId'
                // String status = getPaymentStatusFromMercadoPago(paymentId);

                String mockStatus = "APROVADO"; // Substitua pela busca real do status
                String mockReference = paymentId; // Adapte para a referência correta

                orderService.handlePaymentConfirmation(mockReference, mockStatus);
            }

            // Responda 200 OK para o Mercado Pago saber que você recebeu
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Erro ao processar webhook do Mercado Pago: {}", e.getMessage(), e);
            // Responda com erro, mas o MP pode tentar reenviar
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Você pode adicionar outros endpoints para outros gateways (Stripe, etc.)
}