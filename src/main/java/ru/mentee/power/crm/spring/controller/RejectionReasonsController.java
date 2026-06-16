package ru.mentee.power.crm.spring.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.mentee.power.crm.domain.jpa.RejectionReasons;
import ru.mentee.power.crm.jparepository.RejectionReasonsRepository;

@Controller
@RequestMapping("/admin/rejection-reasons")
public class RejectionReasonsController {

  private final RejectionReasonsRepository repository;

  public RejectionReasonsController(RejectionReasonsRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("reasons", repository.findAll());
    return "admin/rejection-reasons";
  }

  @PostMapping
  public String create(@RequestParam String name) {
    repository.save(new RejectionReasons(name));
    return "redirect:/admin/rejection-reasons";
  }

  @PostMapping("/{id}/toggle")
  public String toggle(@PathVariable UUID id) {
    Optional<RejectionReasons> found = repository.findById(id);
    found.ifPresent(
        reason -> {
          reason.setActive(!reason.getActive());
          repository.save(reason);
        });
    return "redirect:/admin/rejection-reasons";
  }
}
