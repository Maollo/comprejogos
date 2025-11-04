package maollo.comprejogos.controller;

import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import maollo.comprejogos.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/mercado-pago/payment")
    public ResponseEntity<Void> handleMercadoPagoWebhook(@RequestBody Map<String, Object> payload) {

        logger.info("Webhook de Produção Recebido: {}", payload);

        try {
            // 1. O webhook de produção envia "topic"
            String topic = (String) payload.get("topic");

            if ("merchant_order".equals(topic)) {
                // 2. Pegamos a URL do recurso
                String resourceUrl = (String) payload.get("resource");
                if (resourceUrl == null) {
                    logger.error("Webhook 'merchant_order' sem URL de recurso.");
                    return ResponseEntity.badRequest().build();
                }

                // 3. Extraímos o ID da "Merchant Order" da URL
                String merchantOrderIdStr = resourceUrl.substring(resourceUrl.lastIndexOf('/') + 1);
                Long merchantOrderId = Long.parseLong(merchantOrderIdStr);

                // 4. Buscamos a Ordem de Pagamento na API do MP
                MerchantOrderClient client = new MerchantOrderClient();
                MerchantOrder merchantOrder = client.get(merchantOrderId);

                String ourOrderIdStr = merchantOrder.getExternalReference();
                String orderStatus = merchantOrder.getStatus(); // ex: "closed", "opened"

                logger.info("Processando Merchant Order: Nosso Pedido ID {}, Status MP: {}", ourOrderIdStr, orderStatus);

                // 5. Verificamos se foi "closed" (pago e finalizado)
                // (A lógica exata de status do MP pode ser complexa, "closed" é um bom indicador)
                if ("closed".equals(orderStatus) && "paid".equals(merchantOrder.getOrderStatus())) {
                    // 6. Chamamos nosso serviço com o ID do NOSSO pedido
                    orderService.handlePaymentConfirmation(Long.parseLong(ourOrderIdStr), "APROVADO");
                } else {
                    logger.warn("Merchant Order não está 'closed' ou 'paid'. Status: {}", orderStatus);
                }

                return ResponseEntity.ok().build();
            }

            logger.warn("Webhook recebido, mas não é do tipo 'merchant_order'. Tópico: {}", topic);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Erro ao processar webhook de produção: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}