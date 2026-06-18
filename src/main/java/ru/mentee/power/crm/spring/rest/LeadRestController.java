package ru.mentee.power.crm.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;
import ru.mentee.power.crm.spring.mapper.LeadMapper;
import ru.mentee.power.crm.spring.rest.generated.LeadManagementApi;

@RestController
@Validated
public class LeadRestController implements LeadManagementApi {

  private final LeadService leadService;
  private final LeadMapper leadMapper;

  public LeadRestController(LeadService leadService, LeadMapper leadMapper) {
    this.leadService = leadService;
    this.leadMapper = leadMapper;
  }

  @Override
  public ResponseEntity<List<LeadResponse>> getLeads() {
    List<LeadResponse> responses =
        leadService.findAll().stream().map(leadMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<LeadResponse> getLeadById(UUID id) {
    return leadService
        .findById(id)
        .map(leadMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<LeadResponse> createLead(CreateLeadRequest request) {
    LeadDto created =
        leadService.addLead(
            request.getEmail(), request.getCompany(), LeadStatus.valueOf(request.getStatus()));
    URI location = URI.create("/api/leads/" + created.id());
    return ResponseEntity.created(location).body(leadMapper.toResponse(created));
  }

  @Override
  public ResponseEntity<LeadResponse> updateLead(UUID id, UpdateLeadRequest request) {
    LeadDto updated =
        leadService.update(
            id,
            request.getEmail(),
            request.getPhone(),
            request.getCompany(),
            LeadStatus.valueOf(request.getStatus()));
    return ResponseEntity.ok(leadMapper.toResponse(updated));
  }

  @Override
  public ResponseEntity<Void> deleteLead(UUID id) {
    leadService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
