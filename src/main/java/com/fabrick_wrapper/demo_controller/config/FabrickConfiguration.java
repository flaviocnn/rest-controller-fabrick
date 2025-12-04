package com.fabrick_wrapper.demo_controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FabrickConfiguration {

    @Value("${fabrick.base-url}")
    private String baseUrl;

    @Value("${fabrick.api-key}")
    private String apiKey;

    @Bean
    public RestClient fabrickRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Auth-Schema", "S2S")
                .defaultHeader("Api-Key", apiKey)
                .build();
    }
}
