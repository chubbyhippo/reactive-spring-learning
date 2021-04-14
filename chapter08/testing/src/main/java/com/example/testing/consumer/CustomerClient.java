package com.example.testing.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomerClient {
    private final WebClient webClient;
    private String base = "localhost:8080";

    public void setBase(String base) {
        this.base = base;
        log.info("setting base to " + base);

    }

    public Flux<Customer> getAllCustomers() {
        return webClient.get()
                .uri("http://" + this.base + "/customers")
                .retrieve()
                .bodyToFlux(Customer.class);
    }
}
