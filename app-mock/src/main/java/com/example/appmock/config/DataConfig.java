package com.example.appmock.config;

import com.example.appmock.model.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public CustomerRepository customerRepository() {
        return new com.example.appmock.model.InMemoryRepository();
    }
}
