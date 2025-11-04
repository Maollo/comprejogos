package maollo.comprejogos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoConfig.class);

    // Esta linha TENTA ler do application.properties
    @Value("${mercadopago.access_token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        // Este log VAI nos dizer o que o Spring realmente leu
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            logger.warn("====================================================");
            logger.warn("MERCADO PAGO: SDK inicializado com SUCESSO.");
            logger.warn("Token carregado (início): {}", accessToken.substring(0, 10) + "...");
            logger.warn("====================================================");

            // Chame a classe do SDK usando o nome completo
            com.mercadopago.MercadoPagoConfig.setAccessToken(accessToken);

        } else {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("MERCADO PAGO: ERRO CRÍTICO! Access Token NULO ou INVÁLIDO.");
            logger.error("Verifique se 'mercadopago.access_token' existe no seu");
            logger.error("arquivo application.properties e se ele não está vazio.");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}