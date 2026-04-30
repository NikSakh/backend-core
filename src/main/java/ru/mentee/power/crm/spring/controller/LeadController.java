package ru.mentee.power.crm.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.service.LeadService;

import java.util.List;

@RestController
public class LeadController {

  private final LeadService leadService;

  public LeadController(LeadService leadService) {
    this.leadService = leadService;
  }

  @GetMapping("/leads")
  public List<LeadDto> showLeads() {
    return leadService.findAll();
  }
}