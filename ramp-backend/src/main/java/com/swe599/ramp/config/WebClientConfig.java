package com.swe599.ramp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("https://api.openalex.org") // Base URL for all requests
            .defaultHeader("User-Agent",
                "Ramp/1.0 (mailto:ynsmrkrkmaz@gmail.com)")// Add User-Agent header
            .build();
    }
}

