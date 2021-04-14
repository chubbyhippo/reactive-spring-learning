package com.example.testing.consumer;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@Import(ConsumerApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WiremockCustomerClientTest {

    @Autowired
    private CustomerClient client;

    @Autowired
    private Environment environment;

    @BeforeAll
    public void setupWireMock() {
        String base = String.format("%s:%s", "localhost",
                environment.getProperty("wiremock.server.port", Integer.class));
        client.setBase(base);

        String json = "[{ \"id\":\"1\", \"name\":\"Jane\"},"
                + "{ \"id\":\"2\", \"name\":\"John\"}]";

        WireMock.stubFor(WireMock.get("/customers").willReturn(WireMock.aResponse()
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(json)));

    }

    @Test
    public void getAllCustomers() {
        Flux<Customer> customers = this.client.getAllCustomers();
        StepVerifier.create(customers)
                .expectNext(new Customer("1", "Jane"))
                .expectNext(new Customer("2", "John"))
                .verifyComplete();
    }
}
