package ru.mentee.power.crm.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelloCrmServerTest {

  private HelloCrmServer server;
  private static final int TEST_PORT = 8081;
  private static final String BASE_URL = "http://localhost:" + TEST_PORT;

  @BeforeEach
  void setUp() throws Exception {
    server = new HelloCrmServer(TEST_PORT);
    server.start();
  }

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @Test
  void shouldReturnHelloCrmHtmlWhenAccessingHelloEndpoint() throws Exception {
    URL url = new URL(BASE_URL + "/hello");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    String contentType = connection.getHeaderField("Content-Type");

    assertEquals(200, responseCode);
    assertTrue(contentType != null && contentType.contains("text/html"));
    assertTrue(contentType != null && contentType.contains("charset=UTF-8"));

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getInputStream()))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line).append("\n");
      }

      String responseBody = response.toString();
      assertTrue(responseBody.contains("<h1>Hello CRM!</h1>"));
      assertTrue(responseBody.contains("<title>Hello CRM</title>"));
    }
  }

  @Test
  void shouldReturn404ForNonExistentEndpoint() throws Exception {
    URL url = new URL(BASE_URL + "/nonexistent");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    assertEquals(404, responseCode);
  }

  @Test
  void shouldHandleMultipleRequestsCorrectly() throws Exception {
    for (int i = 0; i < 3; i++) {
      URL url = new URL(BASE_URL + "/hello");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();

      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line).append("\n");
        }

        assertEquals(200, responseCode);
        assertTrue(response.toString().contains("<h1>Hello CRM!</h1>"));
      }
    }
  }
}