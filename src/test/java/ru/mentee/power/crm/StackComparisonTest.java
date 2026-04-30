package ru.mentee.power.crm;

import org.junit.jupiter.api.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.assertj.core.api.Assertions.*;

class StackComparisonTest {

  private static final int SERVLET_PORT = 8080;
  private static final int SPRING_PORT = 8081;

  private HttpClient httpClient;

  @BeforeEach
  void setUp() {
    httpClient = HttpClient.newHttpClient();
  }

  @Test
  @DisplayName("Оба стека должны возвращать лидов")
  void shouldReturnLeadsFromBothStacks() throws Exception {
    HttpRequest servletRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads"))
        .GET()
        .build();

    HttpRequest springRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:" + SPRING_PORT + "/leads"))
        .GET()
        .build();

    HttpResponse<String> servletResponse = httpClient.send(
        servletRequest, HttpResponse.BodyHandlers.ofString());
    HttpResponse<String> springResponse = httpClient.send(
        springRequest, HttpResponse.BodyHandlers.ofString());

    assertThat(servletResponse.statusCode())
        .as("Servlet stack should return 200")
        .isEqualTo(200);

    assertThat(springResponse.statusCode())
        .as("Spring Boot stack should return 200")
        .isEqualTo(200);

    assertThat(servletResponse.body()).isNotEmpty();
    assertThat(springResponse.body()).isNotEmpty();

    System.out.printf("Servlet response length: %d%n", servletResponse.body().length());
    System.out.printf("Spring response length: %d%n", springResponse.body().length());
  }

  private int countTableRows(String html) {
    int count = 0;
    int index = 0;
    String search = "<tr>";
    while ((index = html.indexOf(search, index)) != -1) {
      count++;
      index += search.length();
    }
    return count;
  }

  @Test
  @DisplayName("Измерение времени старта обоих стеков")
  void shouldMeasureStartupTime() {
    long servletStartupMs = measureServletStartup();
    long springStartupMs = measureSpringBootStartup();

    System.out.println("=== Сравнение времени старта ===");
    System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
    System.out.printf("Spring Boot: %d ms%n", springStartupMs);
    System.out.printf("Разница: Spring %s на %d ms%n",
        springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
        Math.abs(springStartupMs - servletStartupMs));

    assertThat(servletStartupMs).isLessThan(10_000);
    assertThat(springStartupMs).isLessThan(15_000);
  }

  private long measureServletStartup() {
    long start = System.nanoTime();
    try {
      org.apache.catalina.startup.Tomcat tomcat = new org.apache.catalina.startup.Tomcat();
      tomcat.setPort(0);
      tomcat.getConnector();
      tomcat.start();
      tomcat.stop();
      tomcat.destroy();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    long end = System.nanoTime();
    return (end - start) / 1_000_000;
  }

  private long measureSpringBootStartup() {
    long start = System.nanoTime();
    try {
      org.springframework.context.ConfigurableApplicationContext context =
          org.springframework.boot.SpringApplication.run(
              ru.mentee.power.crm.spring.Application.class,
              "--server.port=0",
              "--spring.main.web-application-type=servlet"
          );
      context.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    long end = System.nanoTime();
    return (end - start) / 1_000_000;
  }

  @AfterEach
  void tearDown() {
    httpClient = null;
  }
}