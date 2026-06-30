package ru.mentee.power.crm.spring.client;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
@WireMockTest(httpPort = 8089)
@ActiveProfiles("test")
class EmailValidationFeignClientContractTest {

  @Autowired private EmailValidationFeignClient feignClient;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("email.validation.base-url", () -> "http://localhost:8089");
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

    EmailValidationResponse response = feignClient.validateEmail("john@example.com");

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

    EmailValidationResponse response = feignClient.validateEmail("invalid-email");

    assertThat(response).isNotNull();
    assertThat(response.valid()).isFalse();
  }

  @Test
  void shouldThrowExceptionWhenExternalServiceReturns500() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(serverError().withBody("Internal Server Error")));

    assertThatThrownBy(() -> feignClient.validateEmail("test@test.com"))
        .isInstanceOf(FeignException.class);
  }

  @Test
  void shouldThrowExceptionWhenExternalServiceTimeouts() {
    stubFor(
        get(urlPathEqualTo("/api/validate/email"))
            .willReturn(okJson("{\"valid\": true}").withFixedDelay(3000)));

    assertThatThrownBy(() -> feignClient.validateEmail("test@test.com"))
        .isInstanceOf(feign.RetryableException.class);
  }
}
