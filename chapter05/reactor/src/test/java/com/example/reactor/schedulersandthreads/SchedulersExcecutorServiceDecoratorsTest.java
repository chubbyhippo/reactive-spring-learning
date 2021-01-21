package com.example.reactor.schedulersandthreads;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.aop.framework.ProxyFactoryBean;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
@TestInstance(Lifecycle.PER_CLASS)
public class SchedulersExcecutorServiceDecoratorsTest {

	private final AtomicInteger methodInvocationCounts = new AtomicInteger();

	private String example = "example";

	private ScheduledExecutorService decorate(
			ScheduledExecutorService executorService) {
		try {
			var pfb = new ProxyFactoryBean();
			pfb.setProxyInterfaces(new Class[]{
					ScheduledExecutorService.class
			});
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName()
						.toLowerCase();

				this.methodInvocationCounts.incrementAndGet();
				log.info("methodName: (" + methodName + ") incrementing...");
				return methodInvocation.proceed();
			});
			pfb.setSingleton(true);
			pfb.setTarget(executorService);
			return (ScheduledExecutorService) pfb.getObject();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	@BeforeAll
	public void beforeAll() {

		Schedulers.resetFactory();

		Schedulers.addExecutorServiceDecorator(this.example,
				(scheduler, scheduledExecutorService) -> this
						.decorate(scheduledExecutorService));
	}

	@Test
	public void changeDefaultDecorator() {
		Flux<Integer> integerFlux = Flux.just(1)
				.delayElements(Duration.ofMillis(1));

		StepVerifier.create(integerFlux).thenAwait(Duration.ofMillis(10))
				.expectNextCount(1).verifyComplete();
		assertThat(this.methodInvocationCounts.get()).isEqualTo(1);
	}

	@AfterAll
	public void after() {
		Schedulers.resetFactory();
		Schedulers.removeExecutorServiceDecorator(this.example);
	}

}
