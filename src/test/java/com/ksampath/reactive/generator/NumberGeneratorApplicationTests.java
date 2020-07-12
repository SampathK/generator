package com.ksampath.reactive.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksampath.reactive.generator.command.GeneratorForm;
import com.ksampath.reactive.generator.controller.Result;
import com.ksampath.reactive.generator.domain.Status;
import com.ksampath.reactive.generator.domain.Task;
import com.ksampath.reactive.generator.service.AlgoGenerator;
import com.ksampath.reactive.generator.service.IdSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class NumberGeneratorApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private AlgoGenerator algoGenerator;

	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testCreateGenerator() {
		final GeneratorForm form = new GeneratorForm(10,2);
		webTestClient.post().uri("/api/v1/generate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(form), GeneratorForm.class)
				.exchange()
				.expectStatus().isAccepted()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$._id").isNotEmpty()
				.jsonPath("$.status").isEqualTo(Status.IN_PROGRESS.toString());
	}

	@SneakyThrows
	@Test
	public void testGeneratorResult() {
		final GeneratorForm form = new GeneratorForm(10,2);
		WebTestClient.BodyContentSpec bodyContentSpec = webTestClient.post().uri("/api/v1/generate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(form), GeneratorForm.class)
				.exchange()
				.expectStatus().isAccepted()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
		bodyContentSpec.jsonPath("$._id").isNotEmpty()
				.jsonPath("$.status").isEqualTo(Status.IN_PROGRESS.toString());
		final String id = mapper.readValue(new String(bodyContentSpec.returnResult().getResponseBody()), Task.class).get_id();
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		webTestClient.get().uri("/api/v1/tasks/{id}/status",id).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody().toString().equals(Status.SUCCESS.toString());
		final Result result = new Result(algoGenerator.apply(form).block());
		webTestClient.get().uri(uriBuilder ->
				uriBuilder
						.path("/api/v1/tasks/{id}/result")
						.queryParam("action", "get_numlist")
						.build(id)).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody().jsonPath("$.result").isEqualTo(result.getResult());
	}

	@SneakyThrows
	@Test
	public void testGenerateErrorResult() {
		final GeneratorForm form = new GeneratorForm(10,-3);
		WebTestClient.BodyContentSpec bodyContentSpec = webTestClient.post().uri("/api/v1/generate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(form), GeneratorForm.class)
				.exchange()
				.expectStatus().isAccepted()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
		bodyContentSpec.jsonPath("$._id").isNotEmpty()
				.jsonPath("$.status").isEqualTo(Status.IN_PROGRESS.toString());
		final String id = mapper.readValue(new String(bodyContentSpec.returnResult().getResponseBody()), Task.class).get_id();
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		webTestClient.get().uri("/api/v1/tasks/{id}/status",id).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody().toString().equals(Status.ERROR.toString());

	}

	@Test
	public void testNotAvailableTaskStatus(){
		final String id = "uuid";
		webTestClient.get().uri("/api/v1/tasks/{id}/status",id).exchange().expectStatus().isNotFound();
	}

	@Test
	public void testUnsupportedActionResultQuery() {
		final String id = "uuid";
		webTestClient.get().uri(uriBuilder ->
				uriBuilder
						.path("/api/v1/tasks/{id}/result")
						.queryParam("action", "unknown")
						.build(id)).exchange().expectStatus().isBadRequest();
	}

	@Test
	public void testNotAvailableTaskResult() {
		final String id = "uuid";
		webTestClient.get().uri(uriBuilder ->
				uriBuilder
						.path("/api/v1/tasks/{id}/result")
						.queryParam("action", "get_numlist")
						.build(id)).exchange().expectStatus().isNotFound();
	}

}
