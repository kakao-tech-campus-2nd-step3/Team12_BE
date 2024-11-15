package katecam.luvicookie.ditto.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DITTO API")
                        .description("스터디 관리 플랫폼 DITTO의 API 문서")
                        .version("1.0.0"));
    }
}
