package ru.mentee.power.crm.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

@Service
public class LeadLockingService {

  private static final Logger LOG = LoggerFactory.getLogger(LeadLockingService.class);

  private final LeadJpaRepository repository;

  public LeadLockingService(LeadJpaRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public LeadJpaEntity updateWithPessimisticLock(UUID id, String newStatus) {
    LeadJpaEntity lead =
        repository
            .findByIdForUpdate(id)
            .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
    lead.setStatus(newStatus);
    return repository.save(lead);
  }

  @Transactional
  public LeadJpaEntity updateWithOptimisticLock(UUID id, String newStatus) {
    try {
      LeadJpaEntity lead =
          repository
              .findById(id)
              .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + id));
      lead.setStatus(newStatus);
      return repository.save(lead);
    } catch (ObjectOptimisticLockingFailureException e) {
      LOG.warn("Optimistic lock conflict for lead {}: {}", id, e.getMessage());
      throw e;
    }
  }
}
