package ru.mentee.power.crm.spring.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@WireMockTest(httpPort = 8089)
class EmailValidationClientWireMockTest {

  private EmailValidationClient emailValidationClient;

  @BeforeEach
  void setUp() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(Duration.ofSeconds(5));
    factory.setReadTimeout(Duration.ofSeconds(1));
    RestTemplate restTemplate = new RestTemplate(factory);
    emailValidationClient = new EmailValidationClient(restTemplate, "http://localhost:8089");
  }

  @Test
  void shouldReturnValidWhenEmailIsCorrect() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("john@example.com"))
            .willReturn(
                okJson(
                    """
                {"email": "john@example.com", "valid": true, "reason": "Email exists"}
                """)));

    EmailValidationResponse response = emailValidationClient.validateEmail("john@example.com");

    assertThat(response).isNotNull();
    assertThat(response.valid()).isTrue();
    assertThat(response.email()).isEqualTo("john@example.com");
  }

  @Test
  void shouldReturnInvalidWhenEmailIsIncorrect() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .withQueryParam("email", equalTo("invalid-email"))
            .willReturn(
                okJson(
                    """
                {"email": "invalid-email", "valid": false, "reason": "Invalid email format"}
                """)));

    EmailValidationResponse response = emailValidationClient.validateEmail("invalid-email");

    assertThat(response).isNotNull();
    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldThrowExceptionWhenExternalServiceReturns500() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Internal Server Error")));

    assertThatThrownBy(() -> emailValidationClient.validateEmail("test@test.com"))
        .isInstanceOf(EmailValidationException.class)
        .hasMessageContaining("Failed to validate email");
  }

  @Test
  void shouldThrowExceptionWhenExternalServiceTimeouts() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(okJson("{\"valid\": true}").withFixedDelay(15000)));

    assertThatThrownBy(() -> emailValidationClient.validateEmail("test@test.com"))
        .isInstanceOf(EmailValidationException.class)
        .hasMessageContaining("Failed to validate email");
  }
}
