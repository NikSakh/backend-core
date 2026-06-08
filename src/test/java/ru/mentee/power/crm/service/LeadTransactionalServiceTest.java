package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.spring.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class LeadTransactionalServiceTest {

  @Autowired
  private LeadTransactionalService service;

  @Test
  void shouldRollbackOnError() {
    LeadJpaEntity lead = service.createLead("rollback@test.com", "TestCorp", "NEW");

    assertThatThrownBy(() -> service.updateLeadAndFail(lead.getId(), "new@test.com"))
        .isInstanceOf(RuntimeException.class);

    LeadJpaEntity found = service.findById(lead.getId()).get();
    assertThat(found.getEmail()).isEqualTo("rollback@test.com");
  }
}