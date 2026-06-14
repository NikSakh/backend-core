package ru.mentee.power.crm.spring.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeadRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private LeadService leadService;

  @Test
  void shouldReturnJsonArrayOfLeads() throws Exception {
    when(leadService.findAll()).thenReturn(List.of(
        new LeadDto("1", "test@example.com", "+123", "Corp", LeadStatus.NEW)
    ));

    MvcResult result = mockMvc.perform(get("/api/leads"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andReturn();

    String body = result.getResponse().getContentAsString();
    assertThat(body).contains("test@example.com");
  }

  @Test
  void shouldReturnLeadById() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.findById(id)).thenReturn(Optional.of(
        new LeadDto(id.toString(), "test@example.com", "+123", "Corp", LeadStatus.NEW)
    ));

    mockMvc.perform(get("/api/leads/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));
  }

  @Test
  void shouldReturn404WhenLeadNotFound() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.findById(id)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/leads/{id}", id))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateLead() throws Exception {
    LeadDto request = new LeadDto(null, "new@test.com", "+123", "NewCorp", LeadStatus.NEW);
    LeadDto response = new LeadDto(UUID.randomUUID().toString(), "new@test.com", "+123", "NewCorp", LeadStatus.NEW);
    when(leadService.addLead(any(), any(), any())).thenReturn(response);

    mockMvc.perform(post("/api/leads")
            .contentType("application/json")
            .content("{\"email\":\"new@test.com\",\"company\":\"NewCorp\",\"status\":\"NEW\"}"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"));
  }
}