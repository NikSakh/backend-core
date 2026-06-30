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

@RestController
@RequestMapping("/api/leads")
public class LeadRestController {

  private final LeadService leadService;

  public LeadRestController(LeadService leadService) {
    this.leadService = leadService;
  }

  @GetMapping
  public ResponseEntity<List<LeadDto>> getAllLeads() {
    return ResponseEntity.ok(leadService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeadDto> getLeadById(@PathVariable UUID id) {
    return leadService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<LeadDto> createLead(@RequestBody LeadDto lead) {
    LeadDto created = leadService.addLead(lead.email(), lead.company(), lead.status());
    URI location = URI.create("/api/leads/" + created.id());
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LeadDto> updateLead(@PathVariable UUID id, @RequestBody LeadDto lead) {
    try {
      LeadDto updated =
          leadService.updateWithRejectionReason(
              id,
              lead.email(),
              lead.phone(),
              lead.company(),
              lead.status(),
              lead.rejectionReasonId());
      return ResponseEntity.ok(updated);
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
