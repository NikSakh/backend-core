package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@WebMvcTest(LeadRestController.class)
class LeadRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LeadService leadService;

  @Test
  void shouldReturn200WhenGetAllLeads() throws Exception {
    when(leadService.findAll())
        .thenReturn(List.of(new LeadDto("1", "test@example.com", "+123", "Corp", LeadStatus.NEW)));

    mockMvc.perform(get("/api/leads")).andExpect(status().isOk());
  }

  @Test
  void shouldReturn404WhenGetNonExistentLead() throws Exception {
    when(leadService.findById(any())).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/leads/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateLead() throws Exception {
    LeadDto response = new LeadDto(UUID.randomUUID().toString(), "new@test.com", "+123", "NewCorp", LeadStatus.NEW);
    when(leadService.addLead(any(), any(), any())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"new@test.com\",\"company\":\"NewCorp\",\"status\":\"NEW\"}"))
        .andExpect(status().isOk());
  }
}