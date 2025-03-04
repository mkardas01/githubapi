package com.example.githubapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${github.token}")
    private String githubToken;
    
    @Value("${github.api.base-url:https://api.github.com}")
    private String githubApiBaseUrl;
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(githubApiBaseUrl)
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultHeader("User-Agent", "Spring Boot Application")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .build();
    }
} 