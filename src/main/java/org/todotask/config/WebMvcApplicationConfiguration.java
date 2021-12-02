package org.todotask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.todotask.config.beanpostprocessor.BeanPostProcessorImp;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"org.springdoc", "org.todotask"})
@PropertySource("classpath:application.properties")
@EnableWebMvc
@Import({org.springdoc.core.SpringDocConfiguration.class,
        org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        org.springdoc.core.SwaggerUiConfigProperties.class,
        org.springdoc.core.SwaggerUiOAuthProperties.class,
        org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class})
public class WebMvcApplicationConfiguration implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
    }

    /**
    *Not deleted because the plans were to substitute the created class in
    *runtime for auto-documentation into the annotation @Schema
    **/
//    @Bean
//    public BeanPostProcessorImp beanPostProcessorImp() {
//        return new BeanPostProcessorImp();
//    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public GroupedOpenApi publicUserApi() {
//        return GroupedOpenApi.builder()
//                .group("User")
//                .pathsToMatch("/user/**")
//                .build();
//    }

    @Bean
    public OpenAPI customOpenApi(@Value("${openapi.application-description}")String appDescription,
                                 @Value("${openapi.application-version}")String appVersion) {
        return new OpenAPI().info(new Info().title("TODOTASK API")
                        .version(appVersion)
                        .description(appDescription)
                        .contact(new Contact().name("dev")
                                .email("todotask@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080/api/v1/")
                                .description("Dev service")));
    }
}
