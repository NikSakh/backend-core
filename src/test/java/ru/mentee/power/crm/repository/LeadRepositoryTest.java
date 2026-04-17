package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.LeadEntity;

class LeadRepositoryTest {

  private LeadRepository repository;
  private LeadEntity leadFirst;
  private LeadEntity leadSecond;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();

    Address addressFirst = new Address("New York", "Main St", "10001");
    Contact contactFirst = new Contact("john@example.com", "+123456789", addressFirst);
    leadFirst = new LeadEntity(UUID.randomUUID(), contactFirst, "TechCorp", "NEW");

    Address addressSecond = new Address("Los Angeles", "Oak Ave", "90001");
    Contact contactSecond = new Contact("jane@example.com", "+987654321", addressSecond);
    leadSecond = new LeadEntity(UUID.randomUUID(), contactSecond, "InnovateInc", "CONTACTED");
  }

  @Test
  void saveShouldSaveLeadAndReturnIt() {
    LeadEntity savedLead = repository.save(leadFirst);
    assertThat(savedLead).isEqualTo(leadFirst);
    assertThat(repository.findById(leadFirst.id().toString())).hasValue(leadFirst);
  }

  @Test
  void findByIdWhenLeadExistsShouldReturnOptionalWithLead() {
    repository.save(leadFirst);
    Optional<LeadEntity> result = repository.findById(leadFirst.id().toString());
    assertThat(result).hasValue(leadFirst);
  }

  @Test
  void findByIdWhenLeadDoesNotExistShouldReturnEmptyOptional() {
    Optional<LeadEntity> result = repository.findById("non-existent-id");
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailWhenLeadWithEmailExistsShouldReturnOptionalWithLead() {
    repository.save(leadFirst);
    Optional<LeadEntity> result = repository.findByEmail("john@example.com");
    assertThat(result).hasValue(leadFirst);
  }

  @Test
  void findByEmailWhenNoLeadWithEmailExistsShouldReturnEmptyOptional() {
    Optional<LeadEntity> result = repository.findByEmail("nonexistent@example.com");
    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailWhenMultipleLeadsWithSameEmailShouldReturnFirstFound() {
    Address duplicateAddress = new Address("Chicago", "Pine St", "60601");
    Contact duplicateContact = new Contact("john@example.com", "+111111111", duplicateAddress);
    LeadEntity duplicateLead = new LeadEntity(UUID.randomUUID(), duplicateContact, "DuplicateCo", "NEW");
    repository.save(leadFirst);
    repository.save(duplicateLead);
    Optional<LeadEntity> result = repository.findByEmail("john@example.com");
    assertThat(result).hasValueSatisfying(lead ->
        assertThat(lead).isEqualTo(leadFirst)
    );
  }

  @Test
  void findAllWhenRepositoryEmptyShouldReturnEmptyList() {
    List<LeadEntity> result = repository.findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void findAllWhenLeadsExistShouldReturnAllLeads() {
    repository.save(leadFirst);
    repository.save(leadSecond);
    List<LeadEntity> result = repository.findAll();
    assertThat(result)
        .hasSize(2)
        .containsExactlyInAnyOrder(leadFirst, leadSecond);
  }

  @Test
  void saveWhenSavingMultipleLeadsShouldStoreAllLeadsCorrectly() {
    repository.save(leadFirst);
    repository.save(leadSecond);
    assertThat(repository.findById(leadFirst.id().toString())).hasValue(leadFirst);
    assertThat(repository.findById(leadSecond.id().toString())).hasValue(leadSecond);
    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  void saveWhenUpdatingExistingLeadShouldReplaceExistingLead() {
    UUID fixedId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    Address originalAddress = new Address("Old City", "Old St", "00000");
    Contact originalContact = new Contact("john@old.com", "+000000000", originalAddress);
    LeadEntity originalLead = new LeadEntity(fixedId, originalContact, "OldCompany", "NEW");

    Address updatedAddress = new Address("New City", "New St", "11111");
    Contact updatedContact = new Contact("john@new.com", "+999999999", updatedAddress);
    LeadEntity updatedLead = new LeadEntity(fixedId, updatedContact, "UpdatedCompany", "CONVERTED");

    repository.save(originalLead);
    LeadEntity savedLead = repository.save(updatedLead);
    assertThat(savedLead).isEqualTo(updatedLead);
    assertThat(repository.findById(originalLead.id().toString())).hasValue(updatedLead);
  }

  @Test
  void findByEmailShouldBeCaseSensitive() {
    repository.save(leadFirst);
    assertThat(repository.findByEmail("JOHN@EXAMPLE.COM")).isEmpty();
    assertThat(repository.findByEmail("john@example.com")).hasValue(leadFirst);
  }

  @Test
  void findAllShouldReturnNewListInstanceEachTime() {
    repository.save(leadFirst);
    List<LeadEntity> firstCall = repository.findAll();
    List<LeadEntity> secondCall = repository.findAll();
    assertThat(firstCall).isEqualTo(secondCall);
    assertThat(firstCall).isNotSameAs(secondCall);
  }
}