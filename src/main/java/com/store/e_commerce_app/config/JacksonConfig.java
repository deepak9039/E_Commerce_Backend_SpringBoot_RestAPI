package com.store.e_commerce_app.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Hibernate6Module hibernate6Module() {
        // Register default module to handle Hibernate proxies and lazy-loading
        return new Hibernate6Module();
    }
}
