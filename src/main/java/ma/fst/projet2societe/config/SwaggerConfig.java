package ma.fst.projet2societe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestion Société REST API")
                        .version("1.0.0")
                        .description("API complète de gestion de projets, employés, phases, affectations et facturation. " +
                                "Construite avec Spring Boot, MySQL, Spring Security et JWT.")
                        .contact(new Contact()
                                .name("Équipe FST")
                                .email("contact@fst.ma"))
                        .license(new License()
                                .name("MIT License")))
                // Bouton "Authorize" dans Swagger UI
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH,
                                new SecurityScheme()
                                        .name(BEARER_AUTH)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Collez ici votre token JWT obtenu via POST /api/auth/login")));
    }
}
