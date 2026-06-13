package ru.mentee.power.crm.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.jpa.DealJpaEntity;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.DealJpaRepository;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

@Service
public class LeadTransactionalService {

  private static final Logger LOG = LoggerFactory.getLogger(LeadTransactionalService.class);

  private final LeadJpaRepository repository;
  private final DealJpaRepository dealRepository;
  private final LeadProcessor leadProcessor;

  public LeadTransactionalService(LeadJpaRepository repository,
                                  DealJpaRepository dealRepository,
                                  LeadProcessor leadProcessor) {
    this.repository = repository;
    this.dealRepository = dealRepository;
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
  public DealJpaEntity convertLeadToDeal(UUID leadId, BigDecimal amount) {
    LeadJpaEntity lead = repository.findById(leadId)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found: " + leadId));
    lead.setStatus("CONVERTED");
    repository.save(lead);

    DealJpaEntity deal = new DealJpaEntity("Deal for lead " + leadId, amount, "NEW");
    return dealRepository.save(deal);
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
      LOG.info("Caught exception in self-invocation: {}", e.getMessage());
    }

    LOG.info("Lead1 status: {}", repository.findById(lead1.getId()).get().getStatus());
    LOG.info("Lead2 email: {}", repository.findById(lead2.getId()).get().getEmail());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateSingleLeadWithNewTransaction(UUID id, String newEmail) {
    LeadJpaEntity lead = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    lead.setEmail(newEmail);
    repository.save(lead);
    throw new RuntimeException("Error in separate transaction!");
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateSingleLeadSuccessfully(UUID id, String newEmail) {
    LeadJpaEntity lead = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Lead not found"));
    lead.setEmail(newEmail);
    repository.save(lead);
  }

  public void processLeadsWithSolution() {
    LeadJpaEntity lead1 = createLead("lead3@example.com", "Corp3", "NEW");
    LeadJpaEntity lead2 = createLead("lead4@example.com", "Corp4", "NEW");

    try {
      leadProcessor.updateSingleLead(lead2.getId(), "updated@example.com");
    } catch (RuntimeException e) {
      LOG.info("Caught exception in processor: {}", e.getMessage());
    }

    LOG.info("Lead3 status: {}", repository.findById(lead1.getId()).get().getStatus());
    LOG.info("Lead4 email: {}", repository.findById(lead2.getId()).get().getEmail());
  }
}