package com.example.restfulusers.api;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // http://localhost:8080/swagger-ui.html

    @Bean
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("public-api")
                .apiInfo(getApiInfo())
                .select()
                .paths(getPaths())
                .build();

//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .apiInfo(getApiInfo())
//                .build();
    }

    private Predicate<String> getPaths() {
        return or(regex("/api/user.*"));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful-Users API")
                .description("RESTful-Users API reference for developers")
                .contact(new Contact("Tanmay Singal", "https://github.com/singaltanmay", "tanmaysingal2013@gmail.com"))
                .version("1.0")
                .build();
    }
}
