package com.example.testing.producer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    public void create() {
        Customer customer = new Customer("123", "foo");
        assertThat(customer.getId()).isEqualTo("123");
        assertThat(customer.getId()).matches("123");
        assertThat(customer.getName()).isEqualToIgnoringWhitespace("foo");
    }

}