package com.example.webflux.routes;

import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriBuilder;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public class LowercaseUriServerRequestWrapper extends ServerRequestWrapper {

	public LowercaseUriServerRequestWrapper(ServerRequest target) {
		super(target);
	}

	@Override
	public URI uri() {
		log.info("uri = " + super.uri().toString());
		log.info("uri lowercase = " + super.uri().toString().toLowerCase());
		return URI.create(super.uri().toString().toLowerCase());
	}

	@Override
	public String path() {
		log.info("raw path = " + uri().getRawPath());
		return uri().getRawPath();
	}

	@Override
	public PathContainer pathContainer() {
		log.info("path container was called");
		return PathContainer.parsePath(path());
	}

	@Override
	public ServerRequest request() {
		// TODO Auto-generated method stub
		return super.request();
	}

	@Override
	public HttpMethod method() {
		// TODO Auto-generated method stub
		return super.method();
	}

	@Override
	public String methodName() {
		// TODO Auto-generated method stub
		return super.methodName();
	}

	@Override
	public UriBuilder uriBuilder() {
		// TODO Auto-generated method stub
		return super.uriBuilder();
	}

	@Override
	public RequestPath requestPath() {
		// TODO Auto-generated method stub
		return super.requestPath();
	}

	@Override
	public Headers headers() {
		// TODO Auto-generated method stub
		return super.headers();
	}

	@Override
	public MultiValueMap<String, HttpCookie> cookies() {
		// TODO Auto-generated method stub
		return super.cookies();
	}

	@Override
	public Optional<InetSocketAddress> remoteAddress() {
		// TODO Auto-generated method stub
		return super.remoteAddress();
	}

	@Override
	public Optional<InetSocketAddress> localAddress() {
		// TODO Auto-generated method stub
		return super.localAddress();
	}

	@Override
	public List<HttpMessageReader<?>> messageReaders() {
		// TODO Auto-generated method stub
		return super.messageReaders();
	}

	@Override
	public <T> T body(BodyExtractor<T, ? super ServerHttpRequest> extractor) {
		// TODO Auto-generated method stub
		return super.body(extractor);
	}

	@Override
	public <T> T body(BodyExtractor<T, ? super ServerHttpRequest> extractor,
			Map<String, Object> hints) {
		// TODO Auto-generated method stub
		return super.body(extractor, hints);
	}

	@Override
	public <T> Mono<T> bodyToMono(Class<? extends T> elementClass) {
		// TODO Auto-generated method stub
		return super.bodyToMono(elementClass);
	}

	@Override
	public <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> typeReference) {
		// TODO Auto-generated method stub
		return super.bodyToMono(typeReference);
	}

	@Override
	public <T> Flux<T> bodyToFlux(Class<? extends T> elementClass) {
		// TODO Auto-generated method stub
		return super.bodyToFlux(elementClass);
	}

	@Override
	public <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> typeReference) {
		// TODO Auto-generated method stub
		return super.bodyToFlux(typeReference);
	}

	@Override
	public Optional<Object> attribute(String name) {
		// TODO Auto-generated method stub
		return super.attribute(name);
	}

	@Override
	public Map<String, Object> attributes() {
		// TODO Auto-generated method stub
		return super.attributes();
	}

	@Override
	public Optional<String> queryParam(String name) {
		// TODO Auto-generated method stub
		return super.queryParam(name);
	}

	@Override
	public MultiValueMap<String, String> queryParams() {
		// TODO Auto-generated method stub
		return super.queryParams();
	}

	@Override
	public String pathVariable(String name) {
		// TODO Auto-generated method stub
		return super.pathVariable(name);
	}

	@Override
	public Map<String, String> pathVariables() {
		// TODO Auto-generated method stub
		return super.pathVariables();
	}

	@Override
	public Mono<WebSession> session() {
		// TODO Auto-generated method stub
		return super.session();
	}

	@Override
	public Mono<? extends Principal> principal() {
		// TODO Auto-generated method stub
		return super.principal();
	}

	@Override
	public Mono<MultiValueMap<String, String>> formData() {
		// TODO Auto-generated method stub
		return super.formData();
	}

	@Override
	public Mono<MultiValueMap<String, Part>> multipartData() {
		// TODO Auto-generated method stub
		return super.multipartData();
	}

	@Override
	public ServerWebExchange exchange() {
		// TODO Auto-generated method stub
		return super.exchange();
	}
	
	

}
