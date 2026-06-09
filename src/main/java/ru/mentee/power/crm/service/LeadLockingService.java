package ru.mentee.power.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

import java.util.UUID;

@Service
public class LeadLockingService {

  private final LeadJpaRepository repository;

  public LeadLockingService(LeadJpaRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public LeadJpaEntity updateWithPessimisticLock(UUID id, String newStatus) {
    LeadJpaEntity lead = repository.findByIdForUpdate(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
    lead.setStatus(newStatus);
    return repository.save(lead);
  }

  @Transactional
  public LeadJpaEntity updateWithOptimisticLock(UUID id, String newStatus) {
    LeadJpaEntity lead = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
    lead.setStatus(newStatus);
    return repository.save(lead);
  }
}
