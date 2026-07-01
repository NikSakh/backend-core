package ru.mentee.power.crm.spring.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.mapper.LeadMapper;

@WebMvcTest(LeadRestController.class)
class LeadRestControllerValidationTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LeadService leadService;

  @MockitoBean private LeadMapper leadMapper;

  @Test
  void shouldReturn400WhenEmailIsBlank() throws Exception {
    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\",\"company\":\"Corp\",\"status\":\"NEW\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenEmailIsInvalid() throws Exception {
    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"notanemail\",\"company\":\"Corp\",\"status\":\"NEW\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenCompanyIsBlank() throws Exception {
    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"company\":\"\",\"status\":\"NEW\"}"))
        .andExpect(status().isBadRequest());
  }
}
