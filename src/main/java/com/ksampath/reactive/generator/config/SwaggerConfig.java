package com.ksampath.reactive.generator.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    @Bean
    public Docket api(@Value("${app.message}") String description,@Value("${spring.application.name}") String applicationName) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ksampath.reactive.generator"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(getApiInfo(description,applicationName));
    }

    private ApiInfo getApiInfo(String applicationName,String description) {
        return new ApiInfo(
                applicationName,
                description,
                "1.0",
                null,
                null,
                "GNU General Public License",
                null,
                Collections.emptyList()
        );
    }
}
