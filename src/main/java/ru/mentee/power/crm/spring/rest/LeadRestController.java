package ru.mentee.power.crm.spring.rest;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;
import ru.mentee.power.crm.spring.mapper.LeadMapper;

@RestController
@RequestMapping("/api/leads")
public class LeadRestController {

  private final LeadService leadService;
  private final LeadMapper leadMapper;

  public LeadRestController(LeadService leadService, LeadMapper leadMapper) {
    this.leadService = leadService;
    this.leadMapper = leadMapper;
  }

  @GetMapping
  public ResponseEntity<List<LeadResponse>> getAllLeads() {
    List<LeadResponse> responses =
        leadService.findAll().stream().map(leadMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeadResponse> getLeadById(@PathVariable UUID id) {
    return leadService
        .findById(id)
        .map(leadMapper::toResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<LeadResponse> createLead(@RequestBody CreateLeadRequest request) {
    LeadDto entity = leadMapper.toEntity(request);
    LeadDto created = leadService.addLead(entity.email(), entity.company(), entity.status());
    URI location = URI.create("/api/leads/" + created.id());
    return ResponseEntity.created(location).body(leadMapper.toResponse(created));
  }

  @PutMapping("/{id}")
  public ResponseEntity<LeadResponse> updateLead(
      @PathVariable UUID id, @RequestBody UpdateLeadRequest request) {
    try {
      LeadDto lead = leadService.findById(id).orElseThrow();
      leadMapper.updateEntity(request, lead);
      LeadDto updated =
          leadService.updateWithRejectionReason(
              id,
              lead.email(),
              lead.phone(),
              lead.company(),
              lead.status(),
              lead.rejectionReasonId());
      return ResponseEntity.ok(leadMapper.toResponse(updated));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLead(@PathVariable UUID id) {
    try {
      leadService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
