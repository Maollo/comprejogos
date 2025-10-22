package maollo.comprejogos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
// Remova o import do SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
// 1. ATUALIZE o @OpenAPIDefinition para REMOVER a linha 'security = ...'
@OpenAPIDefinition(
        info = @Info(
                title = "CompreJogos API",
                version = "1.0",
                description = "API para o sistema de e-commerce de jogos CompreJogos"
        )
        // A linha 'security = @SecurityRequirement(name = "bearerAuth")' foi removida daqui.
)
// 2. MANTENHA o @SecurityScheme
// Isso é o que cria o botão "Authorize" e permite que você insira o token.
@SecurityScheme(
        name = "bearerAuth", // Este é o nome que usaremos nos controllers se quisermos proteger endpoints individualmente
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT obtido no login"
)
public class OpenApiConfig {
    // A classe continua vazia.
}