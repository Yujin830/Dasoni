package signiel.heartsigniel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {
        "signiel.heartsigniel.controller"
})
public class SwaggerConfig {

    /** swagger */
    @Bean
    public Docket MeetingApi() {
        Server serverLocal = new Server("local", "http://localhost:8081", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server server = new Server("test", "http://i9a307.p.ssafy.io", "for testing", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal,server)
                .groupName("Shop API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("signiel.heartsigniel.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.DasoniApiInfo());

    }

    private ApiInfo DasoniApiInfo() {
        return new ApiInfoBuilder()
                .title("다소니 API")
                .description("대한민국 저출산 문제를 해결한 유일한 희망")
                .version("1.0")
                .build();
    }
}