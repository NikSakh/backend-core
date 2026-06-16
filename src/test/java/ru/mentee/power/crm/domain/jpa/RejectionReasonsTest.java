package ru.mentee.power.crm.domain.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

import ru.mentee.power.crm.jparepository.RejectionReasonsRepository;

@Disabled("Requires PostgreSQL — run locally")
@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan(basePackages = "ru.mentee.power.crm.domain.jpa")
@EnableJpaRepositories(basePackages = "ru.mentee.power.crm.jparepository")
class RejectionReasonsTest {

  @Autowired private RejectionReasonsRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldFindOnlyActiveReasons() {
    RejectionReasons active = repository.save(new RejectionReasons("Причина 1"));
    RejectionReasons inactive = repository.save(new RejectionReasons("Причина 2"));
    inactive.setActive(false);
    repository.save(inactive);

    List<RejectionReasons> activeReasons = repository.findByActiveTrue();

    assertThat(activeReasons).hasSize(1);
    assertThat(activeReasons.get(0).getName()).isEqualTo("Причина 1");
  }

  @Test
  void shouldCreateReason() {
    RejectionReasons reason = repository.save(new RejectionReasons("Не прошёл комплаенс"));

    assertThat(reason.getId()).isNotNull();
    assertThat(reason.getName()).isEqualTo("Не прошёл комплаенс");
    assertThat(reason.getActive()).isTrue();
  }

  @Test
  void shouldToggleActiveStatus() {
    RejectionReasons reason = repository.save(new RejectionReasons("Тест"));
    reason.setActive(false);
    repository.save(reason);

    RejectionReasons found = repository.findById(reason.getId()).get();
    assertThat(found.getActive()).isFalse();
  }

  @Test
  void shouldPreserveInactiveReasonInData() {
    RejectionReasons reason = repository.save(new RejectionReasons("Историческая"));
    UUID id = reason.getId();

    reason.setActive(false);
    repository.save(reason);

    RejectionReasons found = repository.findById(id).get();
    assertThat(found.getName()).isEqualTo("Историческая");
    assertThat(found.getActive()).isFalse();
  }
}
