package ru.itis.cal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(value = "ru.itis.cal")
@EnableJpaRepositories(basePackages = "ru.itis.cal.repository")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
// http://localhost:8080/swagger-ui/index.html
@EnableSwagger2
public class ApplicationConfiguration {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
//                .globalOperationParameters(swaggerGlobalParameters())
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.itis.cal.controller"))
                .paths(PathSelectors.any())
                .build();
    }


}


