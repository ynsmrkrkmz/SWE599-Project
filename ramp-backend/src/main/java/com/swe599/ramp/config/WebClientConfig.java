package com.swe599.ramp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("https://api.openalex.org") // Base URL for all requests
            .defaultHeader("User-Agent",
                "Ramp/1.0 (mailto:ynsmrkrkmaz@gmail.com)")// Add User-Agent header
            .defaultHeader("Accept", "application/json")
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                    .maxInMemorySize(10 * 1024 * 1024)) // Increase to 10MB
                .build())
            .build();
    }
}

