package ru.mentee.power.crm.spring.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.mapper.LeadMapper;

@WebMvcTest(LeadRestController.class)
class LeadRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private LeadService leadService;

  @MockitoBean private LeadMapper leadMapper;

  @Test
  void shouldReturn200WhenGetAllLeads() throws Exception {
    LeadDto dto = new LeadDto("1", "test@example.com", "+123", "Corp", LeadStatus.NEW);
    LeadResponse response = new LeadResponse();
    response.setId(UUID.randomUUID());
    response.setEmail("test@example.com");
    response.setPhone("+123");
    response.setCompany("Corp");
    response.setStatus("NEW");
    when(leadService.findAll()).thenReturn(List.of(dto));
    when(leadMapper.toResponse(dto)).thenReturn(response);

    mockMvc.perform(get("/api/leads")).andExpect(status().isOk());
  }

  @Test
  void shouldReturn404WhenGetNonExistentLead() throws Exception {
    when(leadService.findById(any())).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/leads/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void shouldCreateLead() throws Exception {
    LeadDto entity = new LeadDto(null, "new@test.com", "+123", "NewCorp", LeadStatus.NEW);
    LeadDto created =
        new LeadDto(
            UUID.randomUUID().toString(), "new@test.com", "+123", "NewCorp", LeadStatus.NEW);
    LeadResponse response = new LeadResponse();
    response.setId(UUID.fromString(created.id()));
    response.setEmail("new@test.com");
    response.setPhone("+123");
    response.setCompany("NewCorp");
    response.setStatus("NEW");

    when(leadMapper.toEntity(any(CreateLeadRequest.class))).thenReturn(entity);
    when(leadService.addLead(any(), any(), any())).thenReturn(created);
    when(leadMapper.toResponse(created)).thenReturn(response);

    mockMvc
        .perform(
            post("/api/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"new@test.com\",\"company\":\"NewCorp\",\"status\":\"NEW\"}"))
        .andExpect(status().isCreated());
  }

  @Test
  void shouldReturn200WhenUpdateLead() throws Exception {
    UUID id = UUID.randomUUID();
    LeadDto existing =
        new LeadDto(id.toString(), "old@test.com", "+111", "OldCorp", LeadStatus.NEW);
    LeadDto updated =
        new LeadDto(id.toString(), "updated@test.com", "+123", "Corp", LeadStatus.QUALIFIED);
    LeadResponse response = new LeadResponse();
    response.setId(id);
    response.setEmail("updated@test.com");
    response.setPhone("+123");
    response.setCompany("Corp");
    response.setStatus("QUALIFIED");

    when(leadService.findById(id)).thenReturn(Optional.of(existing));
    when(leadService.updateWithRejectionReason(any(), any(), any(), any(), any(), any()))
        .thenReturn(updated);
    when(leadMapper.toResponse(updated)).thenReturn(response);

    mockMvc
        .perform(
            put("/api/leads/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\":\"updated@test.com\",\"company\":\"Corp\",\"status\":\"QUALIFIED\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturn204WhenDeleteExistingLead() throws Exception {
    mockMvc.perform(delete("/api/leads/{id}", UUID.randomUUID())).andExpect(status().isNoContent());
  }
}
