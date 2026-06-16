package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
  void shouldReturn201WithLocationWhenCreateLead() throws Exception {
    String id = UUID.randomUUID().toString();
    when(leadService.addLead(any(), any(), any()))
        .thenReturn(new LeadDto(id, "new@test.com", "+123", "Corp", LeadStatus.NEW));

    mockMvc
        .perform(
            post("/api/leads")
                .contentType("application/json")
                .content("{\"email\":\"new@test.com\",\"company\":\"Corp\",\"status\":\"NEW\"}"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/leads/" + id));
  }

  @Test
  void shouldReturn204WhenDeleteExistingLead() throws Exception {
    mockMvc.perform(delete("/api/leads/{id}", UUID.randomUUID())).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturn404WhenDeleteNonExistentLead() throws Exception {
    doThrow(new RuntimeException()).when(leadService).delete(any());

    mockMvc.perform(delete("/api/leads/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200WhenUpdateLead() throws Exception {
    UUID id = UUID.randomUUID();
    when(leadService.updateWithRejectionReason(any(), any(), any(), any(), any(), any()))
        .thenReturn(
            new LeadDto(id.toString(), "updated@test.com", "+123", "Corp", LeadStatus.QUALIFIED));

    mockMvc
        .perform(
            put("/api/leads/{id}", id)
                .contentType("application/json")
                .content(
                    "{\"email\":\"updated@test.com\","
                        + "\"company\":\"Corp\",\"status\":\"QUALIFIED\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn404WhenUpdateNonExistentLead() throws Exception {
    when(leadService.updateWithRejectionReason(any(), any(), any(), any(), any(), any()))
        .thenThrow(new RuntimeException());

    mockMvc
        .perform(
            put("/api/leads/{id}", UUID.randomUUID())
                .contentType("application/json")
                .content(
                    "{\"email\":\"updated@test.com\","
                        + "\"company\":\"Corp\",\"status\":\"QUALIFIED\"}"))
        .andExpect(status().isNotFound());
  }
}
