package com.example.testing.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CustomerClientConfiguration {

    @Bean
    WebClient myWebClient(WebClient.Builder builder) {
        return builder.build();

    }
}
