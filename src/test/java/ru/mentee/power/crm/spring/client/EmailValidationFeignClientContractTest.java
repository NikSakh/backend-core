package ru.mentee.power.crm.spring.client;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.mentee.power.crm.spring.Application;

@Disabled("Requires PostgreSQL and WireMock — run locally")
@SpringBootTest
@ActiveProfiles("dev")
@ContextConfiguration(classes = Application.class)
class EmailValidationFeignClientContractTest {

  private static final WireMockServer WIRE_MOCK_SERVER =
      new WireMockServer(WireMockConfiguration.options().dynamicPort());

  @Autowired private EmailValidationFeignClient feignClient;

  @BeforeAll
  static void startWireMock() {
    WIRE_MOCK_SERVER.start();
  }

  @AfterAll
  static void stopWireMock() {
    WIRE_MOCK_SERVER.stop();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", () -> WIRE_MOCK_SERVER.baseUrl());
  }

  @Test
  void shouldReturnValidWhenEmailIsCorrect() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(
                okJson(
                    """
                    {"email": "john@example.com", "valid": true, "reason": "OK"}
                    """)));

    EmailValidationResponse response = feignClient.validateEmail("john@example.com");

    assertThat(response.valid()).isTrue();
    assertThat(response.email()).isEqualTo("john@example.com");
  }

  @Test
  void shouldReturnInvalidWhenEmailIsBad() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(
                okJson(
                    """
                    {"email": "bad@email.com", "valid": false, "reason": "Domain not found"}
                    """)));

    EmailValidationResponse response = feignClient.validateEmail("bad@email.com");

    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldThrowWhenServerError() {
    stubFor(get(urlPathEqualTo("/api/validate/email")).willReturn(serverError()));

    assertThatThrownBy(() -> feignClient.validateEmail("any@email.com"))
        .isInstanceOf(feign.FeignException.class);
  }
}
