package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class LeadRepositoryTest {

  private LeadRepository repository;
  private Lead lead1;
  private Lead lead2;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
    lead1 = new Lead(
        "1",
        "john@example.com",
        "+123456789",
        "TechCorp",
        LeadStatus.NEW
    );
    lead2 = new Lead(
        "2",
        "jane@example.com",
        "+987654321",
        "InnovateInc",
        LeadStatus.CONTACTED
    );
  }

  @Test
  void saveShouldSaveLeadAndReturnIt() {
    Lead savedLead = repository.save(lead1);
    assertThat(savedLead).isEqualTo(lead1);
    assertThat(repository.findById("1")).hasValue(lead1);
  }

  @Test
  void findByIdWhenLeadExistsShouldReturnOptionalWithLead() {
    repository.save(lead1);
    Optional<Lead> result = repository.findById("1");
    assertThat(result).hasValue(lead1);
  }

  @Test
  void findByIdWhenLeadDoesNotExistShouldReturnEmptyOptional() {
    Optional<Lead> result = repository.findById("non-existent-id");
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailWhenLeadWithEmailExistsShouldReturnOptionalWithLead() {
    repository.save(lead1);
    Optional<Lead> result = repository.findByEmail("john@example.com");
    assertThat(result).hasValue(lead1);
  }

  @Test
  void findByEmailWhenNoLeadWithEmailExistsShouldReturnEmptyOptional() {
    Optional<Lead> result = repository.findByEmail("nonexistent@example.com");
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailWhenMultipleLeadsWithSameEmailShouldReturnFirstFound() {
    Lead duplicateLead = new Lead(
        "3",
        "john@example.com",
        "+111111111",
        "DuplicateCo",
        LeadStatus.NEW
    );
    repository.save(lead1);
    repository.save(duplicateLead);
    Optional<Lead> result = repository.findByEmail("john@example.com");
    assertThat(result).hasValueSatisfying(lead ->
        assertThat(lead).isEqualTo(lead1)
    );
  }

  @Test
  void findAllWhenRepositoryEmptyShouldReturnEmptyList() {
    List<Lead> result = repository.findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void findAllWhenLeadsExistShouldReturnAllLeads() {
    repository.save(lead1);
    repository.save(lead2);
    List<Lead> result = repository.findAll();
    assertThat(result)
        .hasSize(2)
        .containsExactlyInAnyOrder(lead1, lead2);
  }

  @Test
  void saveWhenSavingMultipleLeadsShouldStoreAllLeadsCorrectly() {
    repository.save(lead1);
    repository.save(lead2);
    assertThat(repository.findById("1")).hasValue(lead1);
    assertThat(repository.findById("2")).hasValue(lead2);
    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void saveWhenUpdatingExistingLeadShouldReplaceExistingLead() {
    Lead originalLead = new Lead(
        "1",
        "john@old.com",
        "+000000000",
        "OldCompany",
        LeadStatus.NEW
    );
    Lead updatedLead = new Lead(
        "1",
        "john@new.com",
        "+999999999",
        "UpdatedCompany",
        LeadStatus.CONVERTED
    );
    repository.save(originalLead);
    Lead savedLead = repository.save(updatedLead);
    assertThat(savedLead).isEqualTo(updatedLead);
    assertThat(repository.findById("1")).hasValue(updatedLead);
  }

  @Test
  void findByEmailShouldBeCaseSensitive() {
    repository.save(lead1);
    assertThat(repository.findByEmail("JOHN@EXAMPLE.COM")).isEmpty();
    assertThat(repository.findByEmail("john@example.com")).hasValue(lead1);
  }

  @Test
  void findAllShouldReturnNewListInstanceEachTime() {
    repository.save(lead1);
    List<Lead> firstCall = repository.findAll();
    List<Lead> secondCall = repository.findAll();
    assertThat(firstCall).isEqualTo(secondCall);
    assertThat(firstCall).isNotSameAs(secondCall);
  }
}