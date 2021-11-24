package org.todotask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Configuration
@ComponentScan("org.todotask")
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class WebMvcApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource db = new DriverManagerDataSource();
        db.setDriverClassName(Objects.requireNonNull(env.getProperty("database.driver")));
        db.setUrl(Objects.requireNonNull(env.getProperty("database.url")));
        db.setUsername(Objects.requireNonNull(env.getProperty("database.username")));
        db.setPassword(Objects.requireNonNull(env.getProperty("database.password")));
        return db;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("Users")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(@Value("${openapi.application-description}")String appDescription,
                                 @Value("${openapi.application-version}")String appVersion) {
        return new OpenAPI().info(new Info().title("TODOTASK API")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org"))
                        .contact(new Contact().name("username")
                                .email("test@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                                .description("Dev service"),
                        new Server().url("http://localhost:8082")
                                .description("Beta service")));
    }
}
