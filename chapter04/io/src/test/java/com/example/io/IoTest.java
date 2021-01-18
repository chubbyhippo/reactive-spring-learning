package com.example.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@TestInstance(Lifecycle.PER_CLASS)
public class IoTest {

	private final AtomicLong count = new AtomicLong();

	private final Consumer<Bytes> bytesConsumer = bytes -> count
			.getAndAccumulate(
					bytes.getLength(),
					(previousValue, updateValue) -> previousValue
							+ updateValue);

	private final Resource resource = new ClassPathResource("/data.txt");

	private final Io io = new Io();

	private final File file = Files.createTempFile("io-test-data", ".txt")
			.toFile();

	private final CountDownLatch latch = new CountDownLatch(1);

	private final Runnable onceDone = () -> {
		log.info("counted " + count.get() + " bytes");
		latch.countDown();
	};

	public IoTest() throws IOException {
	}

	@BeforeAll
	public void before() throws IOException {
		count.set(0);
		try (InputStream in = resource.getInputStream();
				OutputStream out = new FileOutputStream(file)) {
			FileCopyUtils.copy(in, out);
		}
	}

	@AfterAll
	public void tearDown() {
		if (file.exists()) {
			assertThat(file.delete()).isTrue();
		}
	}

	@Test
	public void synchronousRead() {
		test(() -> io.synchronousRead(file, bytesConsumer, onceDone));
	}

	@Test
	public void asynchronousRead() {
		test(() -> io.asynchronousRead(file, bytesConsumer,
				onceDone));
	}

	private void test(Runnable r) {
		try {
			r.run();
			latch.await();
			assertThat(count.get()).isEqualTo(file.length());
		} catch (InterruptedException e) {
			log.error(e);
		}

	}

}