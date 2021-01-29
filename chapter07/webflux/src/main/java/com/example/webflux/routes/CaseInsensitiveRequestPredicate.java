package com.example.webflux.routes;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class CaseInsensitiveRequestPredicate implements RequestPredicate {

	private final RequestPredicate target;

	public static RequestPredicate i(RequestPredicate rp) {
		return new CaseInsensitiveRequestPredicate(rp);
	}

	@Override
	public boolean test(ServerRequest request) {
		log.info("test was called");
		log.info("test content was " + new LowercaseUriServerRequestWrapper(request).uri());
		log.info("test result was " + target.test(new LowercaseUriServerRequestWrapper(request)));
		return target.test(new LowercaseUriServerRequestWrapper(request));
	}

	@Override
	public String toString() {
		log.info("target = " + target.toString());
		return target.toString();
	}

}