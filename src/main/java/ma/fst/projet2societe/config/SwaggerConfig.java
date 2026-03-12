package ma.fst.projet2societe.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Societe REST API")
                        .version("1.0.0")
                        .description("A full Mangement Project API built with Spring Boot, MySQL and Swagger UI. " +
                                "Manage Employees,etc ....")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("you@example.com"))
                        .license(new License()
                                .name("MIT License")));
    }
}
