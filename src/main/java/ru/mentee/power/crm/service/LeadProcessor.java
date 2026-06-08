package ru.mentee.power.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

import java.util.UUID;

@Service
public class LeadProcessor {

  private final LeadJpaRepository repository;

  public LeadProcessor(LeadJpaRepository repository) {
    this.repository = repository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateSingleLead(UUID id, String newEmail) {
    LeadJpaEntity lead = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    lead.setEmail(newEmail);
    repository.save(lead);
    throw new RuntimeException("Error in separate transaction!");
  }
}
