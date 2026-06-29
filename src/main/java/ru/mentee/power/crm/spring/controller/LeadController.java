package ru.mentee.power.crm.spring.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.jparepository.RejectionReasonsRepository;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@Controller
public class LeadController {

  private final LeadService leadService;

  private final RejectionReasonsRepository rejectionReasonsRepository;

  public LeadController(
      LeadService leadService, RejectionReasonsRepository rejectionReasonsRepository) {
    this.leadService = leadService;
    this.rejectionReasonsRepository = rejectionReasonsRepository;
  }

  @GetMapping("/leads")
  public String showLeads(
      @RequestParam(required = false) LeadStatus status,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String statusFilter,
      Model model) {
    List<LeadDto> leads;

    if (search != null && !search.isEmpty() || statusFilter != null && !statusFilter.isEmpty()) {
      leads = leadService.findLeads(search, statusFilter);
    } else if (status != null) {
      leads = leadService.findByStatus(status);
    } else {
      leads = leadService.findAll();
    }

    model.addAttribute("leads", leads);
    model.addAttribute("currentFilter", status);
    model.addAttribute("search", search != null ? search : "");
    model.addAttribute("statusFilter", statusFilter != null ? statusFilter : "");
    return "leads/list";
  }

  @GetMapping("/leads/new")
  public String showCreateForm(Model model) {
    model.addAttribute("lead", new LeadDto(null, "", "", "", LeadStatus.NEW));
    return "leads/create";
  }

  @PostMapping("/leads")
  public String createLead(@Valid @ModelAttribute LeadDto lead, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("errors", result);
      return "leads/create";
    }
    leadService.addLead(lead.email(), lead.company(), lead.status());
    return "redirect:/leads";
  }

  @GetMapping("/leads/{id}/edit")
  public String showEditForm(@PathVariable UUID id, Model model) {
    Optional<LeadDto> leadOpt = leadService.findById(id);
    if (leadOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
    }
    model.addAttribute("lead", leadOpt.get());
    model.addAttribute("rejectionReasons", rejectionReasonsRepository.findByActiveTrue());
    return "leads/edit";
  }

  @PostMapping("/leads/{id}")
  public String updateLead(
      @PathVariable UUID id, @Valid @ModelAttribute LeadDto lead, BindingResult result) {
    if (result.hasErrors()) {
      return "leads/edit";
    }
    leadService.updateWithRejectionReason(
        id, lead.email(), lead.phone(), lead.company(), lead.status(), lead.rejectionReasonId());
    return "redirect:/leads";
  }

  @PostMapping("/leads/{id}/delete")
  public String deleteLead(@PathVariable UUID id) {
    leadService.delete(id);
    return "redirect:/leads";
  }
}
