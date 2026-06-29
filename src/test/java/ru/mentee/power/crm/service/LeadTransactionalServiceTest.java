package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.DealJpaEntity;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jparepository.DealJpaRepository;
import ru.mentee.power.crm.jparepository.LeadJpaRepository;

@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@ActiveProfiles("test")
class LeadTransactionalServiceTest {

  @Autowired private LeadTransactionalService service;

  @Autowired private LeadJpaRepository leadRepository;

  @Autowired private DealJpaRepository dealRepository;

  @BeforeEach
  void setUp() {
    dealRepository.deleteAll();
    leadRepository.deleteAll();
  }

  @Test
  void shouldRollbackOnError() {
    LeadJpaEntity lead = service.createLead("rollback@test.com", "TestCorp", "NEW");
    UUID leadId = lead.getId();

    assertThatThrownBy(() -> service.updateLeadAndFail(leadId, "new@test.com"))
        .isInstanceOf(RuntimeException.class);

    LeadJpaEntity found = leadRepository.findById(leadId).get();
    assertThat(found.getEmail()).isEqualTo("rollback@test.com");
  }

  @Test
  void shouldConvertLeadToDeal() {
    LeadJpaEntity lead = service.createLead("convert@test.com", "TestCorp", "QUALIFIED");
    DealJpaEntity deal = service.convertLeadToDeal(lead.getId(), new BigDecimal("100000"));

    assertThat(deal.getId()).isNotNull();
    assertThat(deal.getAmount()).isEqualTo(new BigDecimal("100000"));

    LeadJpaEntity updatedLead = leadRepository.findById(lead.getId()).get();
    assertThat(updatedLead.getStatus()).isEqualTo("CONVERTED");
  }

  @Test
  void shouldDemonstrateSelfInvocationProblem() {
    LeadJpaEntity lead1 = service.createLead("self1@test.com", "Corp1", "NEW");
    LeadJpaEntity lead2 = service.createLead("self2@test.com", "Corp2", "NEW");

    service.processLeadsWithSelfInvocation();

    LeadJpaEntity found1 = leadRepository.findById(lead1.getId()).get();
    LeadJpaEntity found2 = leadRepository.findById(lead2.getId()).get();

    assertThat(found1.getStatus()).isEqualTo("NEW");
    assertThat(found2.getEmail()).isEqualTo("self2@test.com");
  }

  @Test
  void shouldDemonstrateSolutionWithProcessor() {
    LeadJpaEntity lead1 = service.createLead("sol1@test.com", "Corp1", "NEW");
    LeadJpaEntity lead2 = service.createLead("sol2@test.com", "Corp2", "NEW");

    service.processLeadsWithSolution();

    LeadJpaEntity found1 = leadRepository.findById(lead1.getId()).get();
    LeadJpaEntity found2 = leadRepository.findById(lead2.getId()).get();

    assertThat(found1.getStatus()).isEqualTo("NEW");
    assertThat(found2.getEmail()).isEqualTo("sol2@test.com");
  }

  @Test
  void shouldUpdateSingleLeadSuccessfully() {
    LeadJpaEntity lead = service.createLead("success@test.com", "Corp", "NEW");

    service.updateSingleLeadSuccessfully(lead.getId(), "updated@test.com");

    LeadJpaEntity found = leadRepository.findById(lead.getId()).get();
    assertThat(found.getEmail()).isEqualTo("updated@test.com");
  }
}

