package org.example.demo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	// Создаем два контейнера
	private static final GenericContainer<?> container1 = new GenericContainer<>(DockerImageName.parse("devapp:latest"))
			.withExposedPorts(8080);

	private static final GenericContainer<?> container2 = new GenericContainer<>(DockerImageName.parse("prodapp:latest"))
			.withExposedPorts(8081);

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	public static void setUp() {
		// Запускаем контейнеры перед началом тестов
		container1.start();
		container2.start();
	}

	@Test
	void testContainer1Response() {
		// Получаем маппинг порта для первого контейнера
		int mappedPort = container1.getMappedPort(8080);

		// Делаем GET-запрос к первому контейнеру
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + mappedPort,
				String.class
		);

		// Проверяем статус код и содержимое ответа
		assertEquals(200, response.getStatusCodeValue(), "Статус код должен быть 200");
		assertEquals("Expected Response from Container 1", response.getBody(), "Ответ не соответствует ожидаемому");
	}

	@Test
	void testContainer2Response() {
		// Получаем маппинг порта для второго контейнера
		int mappedPort = container2.getMappedPort(8081);

		// Делаем GET-запрос ко второму контейнеру
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + mappedPort,
				String.class
		);

		// Проверяем статус код и содержимое ответа
		assertEquals(200, response.getStatusCodeValue(), "Статус код должен быть 200");
		assertEquals("Expected Response from Container 2", response.getBody(), "Ответ не соответствует ожидаемому");
	}
}