package com.example.webclient.client.timer;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
class TimingWebClientCustomizer implements WebClientCustomizer {

	@Override
	public void customize(WebClient.Builder webClientBuilder) {
		webClientBuilder.filter(new TimingExchangeFilterFunction()); // <1>
	}

}
