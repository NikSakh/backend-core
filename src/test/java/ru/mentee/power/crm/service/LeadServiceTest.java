package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

class LeadServiceTest {

  private LeadRepository repository;
  private LeadService service;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
    service = new LeadService(repository);
  }

  @Test
  void addLeadWhenValidDataShouldCreateAndSaveLead() {
    Lead result = service.addLead("test@example.com", "Test Company", LeadStatus.NEW);

    assertThat(result).isNotNull();
    assertThat(result.email()).isEqualTo("test@example.com");
    assertThat(result.company()).isEqualTo("Test Company");
    assertThat(result.status()).isEqualTo(LeadStatus.NEW);
    Optional<Lead> found = repository.findById(result.id());
    assertThat(found).hasValue(result);
  }

  @Test
  void addLeadWhenEmailIsNullShouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> service.addLead(null, "Test Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be null or empty");
  }

  @Test
  void addLeadWhenEmailIsEmptyShouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> service.addLead("   ", "Test Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be null or empty");
  }

  @Test
  void addLeadWhenCompanyIsNullShouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> service.addLead("test@example.com", null, LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Company cannot be null or empty");
  }

  @Test
  void addLeadWhenCompanyIsEmptyShouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> service.addLead("test@example.com", "   ", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Company cannot be null or empty");
  }

  @Test
  void addLeadWhenStatusIsNullShouldThrowIllegalArgumentException() {
    assertThatThrownBy(() -> service.addLead("test@example.com", "Test Company", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Status cannot be null");
  }

  @Test
  void addLeadWhenLeadWithEmailExistsShouldThrowIllegalStateException() {
    service.addLead("existing@example.com", "Existing Company", LeadStatus.NEW);

    assertThatThrownBy(() -> service.addLead("existing@example.com",
        "Another Company", LeadStatus.NEW))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email 'existing@example.com' already exists");
  }

  @Test
  void addLeadShouldNormalizeEmail() {
    Lead result = service.addLead("  TEST@EXAMPLE.COM  ", "Test Company", LeadStatus.NEW);

    assertThat(result.email()).isEqualTo("test@example.com");
  }

  @Test
  void findAllShouldReturnAllLeads() {
    service.addLead("lead1@example.com", "Company 1", LeadStatus.NEW);
    service.addLead("lead2@example.com", "Company 2", LeadStatus.CONTACTED);

    List<Lead> result = service.findAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void findByIdWhenLeadExistsShouldReturnOptionalWithLead() {
    Lead lead = service.addLead("test@example.com", "Test Company", LeadStatus.NEW);
    UUID id = UUID.fromString(lead.id());

    Optional<Lead> result = service.findById(id);

    assertThat(result).hasValue(lead);
  }

  @Test
  void findByIdWhenLeadDoesNotExistShouldReturnEmptyOptional() {
    Optional<Lead> result = service.findById(UUID.randomUUID());

    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailWhenLeadExistsShouldReturnOptionalWithLead() {
    service.addLead("search@example.com", "Search Company", LeadStatus.NEW);

    Optional<Lead> result = service.findByEmail("search@example.com");

    assertThat(result).isNotEmpty();
  }

  @Test
  void findByEmailWhenLeadDoesNotExistShouldReturnEmptyOptional() {
    Optional<Lead> result = service.findByEmail("nonexistent@example.com");

    assertThat(result).isEmpty();
  }
}