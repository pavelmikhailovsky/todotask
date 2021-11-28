package org.todotask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DataBaseConfiguration {

    @Autowired
    public Environment env;

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

}
