package ru.mentee.power.crm.spring.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public List<LeadDto> getAllLeads() {
    return leadService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeadDto> getLeadById(@PathVariable UUID id) {
    Optional<LeadDto> lead = leadService.findById(id);
    return lead.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public LeadDto createLead(@RequestBody LeadDto lead) {
    return leadService.addLead(lead.email(), lead.company(), lead.status());
  }
}
