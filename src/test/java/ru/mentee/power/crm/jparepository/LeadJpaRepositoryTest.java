package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.jpa.JpaConfig;
import ru.mentee.power.crm.spring.Application;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class, JpaConfig.class})
class LeadJpaRepositoryTest {

  @Autowired private LeadJpaRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldSaveAndFindById() {
    LeadJpaEntity lead = new LeadJpaEntity("test@example.com", "ACME", "NEW");
    LeadJpaEntity saved = repository.save(lead);

    Optional<LeadJpaEntity> found = repository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  void shouldFindByEmail() {
    repository.save(new LeadJpaEntity("john@example.com", "Corp", "NEW"));

    Optional<LeadJpaEntity> found = repository.findByEmail("john@example.com");
    assertThat(found).isPresent();
  }

  @Test
  void shouldFindByStatus() {
    repository.save(new LeadJpaEntity("a@test.com", "A", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "B", "CONTACTED"));

    List<LeadJpaEntity> found = repository.findByStatus("NEW");
    assertThat(found).hasSize(1);
  }

  @Test
  void shouldCountByStatus() {
    repository.save(new LeadJpaEntity("a@test.com", "A", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "B", "NEW"));

    long count = repository.countByStatus("NEW");
    assertThat(count).isEqualTo(2);
  }

  @Test
  void shouldExistByEmail() {
    repository.save(new LeadJpaEntity("john@test.com", "Corp", "NEW"));

    assertThat(repository.existsByEmail("john@test.com")).isTrue();
    assertThat(repository.existsByEmail("nonexistent@test.com")).isFalse();
  }

  @Test
  void shouldFindByStatusAndCompany() {
    repository.save(new LeadJpaEntity("a@test.com", "ACME", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "ACME", "CONTACTED"));
    repository.save(new LeadJpaEntity("c@test.com", "Other", "NEW"));

    List<LeadJpaEntity> found = repository.findByStatusAndCompany("NEW", "ACME");
    assertThat(found).hasSize(1);
  }

  @Test
  void shouldFindByStatusIn() {
    repository.save(new LeadJpaEntity("a@test.com", "A", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "B", "CONTACTED"));
    repository.save(new LeadJpaEntity("c@test.com", "C", "QUALIFIED"));

    List<LeadJpaEntity> found = repository.findByStatusIn(List.of("NEW", "CONTACTED"));
    assertThat(found).hasSize(2);
  }

  @Test
  void shouldFindAllWithPageable() {
    repository.save(new LeadJpaEntity("a@test.com", "A", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "B", "NEW"));
    repository.save(new LeadJpaEntity("c@test.com", "C", "NEW"));

    Page<LeadJpaEntity> page = repository.findAll(PageRequest.of(0, 2));

    assertThat(page.getContent()).hasSize(2);
    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getTotalPages()).isEqualTo(2);
    assertThat(page.getNumber()).isEqualTo(0);
  }

  @Test
  void shouldFindByStatusWithPageable() {
    repository.save(new LeadJpaEntity("a@test.com", "A", "NEW"));
    repository.save(new LeadJpaEntity("b@test.com", "B", "NEW"));
    repository.save(new LeadJpaEntity("c@test.com", "C", "NEW"));

    Page<LeadJpaEntity> page = repository.findByStatus("NEW", PageRequest.of(0, 2));

    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getTotalPages()).isEqualTo(2);
  }

  @Test
  void shouldFindByEmailNative() {
    repository.save(new LeadJpaEntity("native@test.com", "Corp", "NEW"));

    Optional<LeadJpaEntity> found = repository.findByEmailNative("native@test.com");
    assertThat(found).isPresent();
    assertThat(found.get().getCompany()).isEqualTo("Corp");
  }
}

