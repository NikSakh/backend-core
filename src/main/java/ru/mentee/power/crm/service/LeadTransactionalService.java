package ru.mentee.power.crm.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

@Service
public class LeadTransactionalService {

  private final LeadJpaRepository repository;
  private final LeadProcessor leadProcessor;

  public LeadTransactionalService(LeadJpaRepository repository, LeadProcessor leadProcessor) {
    this.repository = repository;
    this.leadProcessor = leadProcessor;
  }

  public Page<LeadJpaEntity> getFirstPage(int pageSize) {
    PageRequest pageRequest = PageRequest.of(0, pageSize);
    return repository.findAll(pageRequest);
  }

  @Transactional
  public LeadJpaEntity createLead(String email, String company, String status) {
    LeadJpaEntity lead = new LeadJpaEntity(email, company, status);
    return repository.save(lead);
  }

  @Transactional
  public LeadJpaEntity updateLeadAndFail(UUID id, String newEmail) {
    Optional<LeadJpaEntity> found = repository.findById(id);
    if (found.isEmpty()) {
      throw new IllegalArgumentException("Lead not found: " + id);
    }
    LeadJpaEntity lead = found.get();
    lead.setEmail(newEmail);
    repository.save(lead);

    throw new RuntimeException("Simulated error — transaction should rollback!");
  }

  @Transactional(readOnly = true)
  public Optional<LeadJpaEntity> findById(UUID id) {
    return repository.findById(id);
  }

  public void processLeadsWithSelfInvocation() {
    LeadJpaEntity lead1 = createLead("lead1@example.com", "Corp1", "NEW");
    LeadJpaEntity lead2 = createLead("lead2@example.com", "Corp2", "NEW");

    try {
      updateSingleLeadWithNewTransaction(lead2.getId(), "updated@example.com");
    } catch (RuntimeException e) {
      System.out.println("Caught exception: " + e.getMessage());
    }

    System.out.println("Lead1 status: " + repository.findById(lead1.getId()).get().getStatus());
    System.out.println("Lead2 email: " + repository.findById(lead2.getId()).get().getEmail());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateSingleLeadWithNewTransaction(UUID id, String newEmail) {
    LeadJpaEntity lead = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    lead.setEmail(newEmail);
    repository.save(lead);
    throw new RuntimeException("Error in separate transaction!");
  }

  public void processLeadsWithSolution() {
    LeadJpaEntity lead1 = createLead("lead3@example.com", "Corp3", "NEW");
    LeadJpaEntity lead2 = createLead("lead4@example.com", "Corp4", "NEW");

    try {
      leadProcessor.updateSingleLead(lead2.getId(), "updated@example.com");
    } catch (RuntimeException e) {
      System.out.println("Caught exception: " + e.getMessage());
    }

    System.out.println("Lead3 status: " + repository.findById(lead1.getId()).get().getStatus());
    System.out.println("Lead4 email: " + repository.findById(lead2.getId()).get().getEmail());
  }
}