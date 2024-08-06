package spotlight.spotlight_ver2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spotlight API")
                        .version("1.0")
                        .description("Spring Boot REST API documentation using springdoc-openapi and OpenAPI 3."));
    }
}
