package ru.mentee.power.crm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

public class LeadService {

  private final LeadRepository repository;

  public LeadService(LeadRepository repository) {
    this.repository = repository;
  }

  public Lead addLead(String email, String company, LeadStatus status) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }
    if (company == null || company.trim().isEmpty()) {
      throw new IllegalArgumentException("Company cannot be null or empty");
    }
    if (status == null) {
      throw new IllegalArgumentException("Status cannot be null");
    }

    String normalizedEmail = email.trim().toLowerCase();

    Optional<Lead> existing = repository.findByEmail(normalizedEmail);
    if (existing.isPresent()) {
      throw new IllegalStateException(
          String.format("Lead with email '%s' already exists (ID: %s)",
              normalizedEmail, existing.get().id())
      );
    }

    Lead lead = new Lead(
        UUID.randomUUID().toString(),
        normalizedEmail,
        null,
        company.trim(),
        status
    );

    return repository.save(lead);
  }

  public List<Lead> findAll() {
    return repository.findAll();
  }

  public Optional<Lead> findById(UUID id) {
    return repository.findById(id.toString());
  }

  public Optional<Lead> findByEmail(String email) {
    return repository.findByEmail(email);
  }
}