package org.remind.melembraai.infra.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "BearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition(
        info = @Info(title = "Me Lembra Ai API", version = "1.0", contact = @Contact(name = "Pedro Oliveira", email = "012pedropaulo@gmail" +
                ".com")),
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
)
public class OpenApiConfig {
}
