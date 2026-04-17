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
  private LeadEntity leadThird;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();

    Address addressFirst = new Address("New York", "Main St", "10001");
    Contact contactFirst = new Contact("john@example.com", "+123456789", addressFirst);
    leadFirst = new LeadEntity(UUID.randomUUID(), contactFirst, "TechCorp", "NEW");

    Address addressSecond = new Address("Los Angeles", "Oak Ave", "90001");
    Contact contactSecond = new Contact("jane@example.com", "+987654321", addressSecond);
    leadSecond = new LeadEntity(UUID.randomUUID(), contactSecond, "InnovateInc", "QUALIFIED");

    Address addressThird = new Address("Chicago", "Pine St", "60601");
    Contact contactThird = new Contact("bob@example.com", "+555555555", addressThird);
    leadThird = new LeadEntity(UUID.randomUUID(), contactThird, "DevWorks", "CONVERTED");
  }

  @Test
  void saveShouldStoreLeadAndReturnIt() {
    LeadEntity savedLead = repository.save(leadFirst);

    assertThat(savedLead).isEqualTo(leadFirst);
    assertThat(repository.findById(leadFirst.id().toString())).hasValue(leadFirst);
  }

  @Test
  void saveShouldAddMultipleLeads() {
    repository.save(leadFirst);
    repository.save(leadSecond);
    repository.save(leadThird);

    assertThat(repository.findAll()).hasSize(3);
    assertThat(repository.findById(leadFirst.id().toString())).hasValue(leadFirst);
    assertThat(repository.findById(leadSecond.id().toString())).hasValue(leadSecond);
    assertThat(repository.findById(leadThird.id().toString())).hasValue(leadThird);
  }

  @Test
  void saveShouldReplaceExistingLeadWithSameId() {
    UUID fixedId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    Address originalAddress = new Address("Old City", "Old St", "00000");
    Contact originalContact = new Contact("old@example.com", "+000000000", originalAddress);
    LeadEntity originalLead = new LeadEntity(fixedId, originalContact, "OldCompany", "NEW");

    Address updatedAddress = new Address("New City", "New St", "11111");
    Contact updatedContact = new Contact("new@example.com", "+999999999", updatedAddress);
    LeadEntity updatedLead = new LeadEntity(fixedId, updatedContact, "UpdatedCompany", "QUALIFIED");

    repository.save(originalLead);
    LeadEntity savedLead = repository.save(updatedLead);

    assertThat(savedLead).isEqualTo(updatedLead);
    assertThat(repository.findById(fixedId.toString())).hasValue(updatedLead);
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void findByIdShouldReturnEmptyWhenIdNotFound() {
    repository.save(leadFirst);

    Optional<LeadEntity> result = repository.findById("non-existent-id");

    assertThat(result).isEmpty();
  }

  @Test
  void findByIdShouldReturnLeadWhenIdExists() {
    repository.save(leadFirst);
    repository.save(leadSecond);

    Optional<LeadEntity> result = repository.findById(leadFirst.id().toString());

    assertThat(result).hasValue(leadFirst);
  }

  @Test
  void findByIdShouldReturnCorrectLeadWhenMultipleLeadsExist() {
    repository.save(leadFirst);
    repository.save(leadSecond);
    repository.save(leadThird);

    Optional<LeadEntity> result = repository.findById(leadSecond.id().toString());

    assertThat(result).hasValue(leadSecond);
  }

  @Test
  void findByEmailShouldReturnEmptyWhenEmailNotFound() {
    repository.save(leadFirst);

    Optional<LeadEntity> result = repository.findByEmail("nonexistent@example.com");

    assertThat(result).isEmpty();
  }

  @Test
  void findByEmailShouldReturnLeadWhenEmailExists() {
    repository.save(leadFirst);
    repository.save(leadSecond);

    Optional<LeadEntity> result = repository.findByEmail("john@example.com");

    assertThat(result).hasValue(leadFirst);
  }

  @Test
  void findByEmailShouldBeCaseSensitive() {
    repository.save(leadFirst);

    Optional<LeadEntity> resultUpper = repository.findByEmail("JOHN@EXAMPLE.COM");
    Optional<LeadEntity> resultLower = repository.findByEmail("john@example.com");

    assertThat(resultUpper).isEmpty();
    assertThat(resultLower).hasValue(leadFirst);
  }

  @Test
  void findByEmailShouldReturnFirstFoundWhenMultipleLeadsHaveSameEmail() {
    UUID idFirst = UUID.randomUUID();
    UUID idSecond = UUID.randomUUID();

    Address addressFirst = new Address("CityFirst", "StreetFirst", "11111");
    Contact contactFirst = new Contact("same@example.com", "+111111111", addressFirst);
    LeadEntity leadWithSameEmailFirst = new LeadEntity(idFirst, contactFirst,
        "Company First", "NEW");

    Address addressSecond = new Address("CitySecond", "StreetSecond", "22222");
    Contact contactSecond = new Contact("same@example.com", "+222222222", addressSecond);
    LeadEntity leadWithSameEmailSecond = new LeadEntity(idSecond, contactSecond,
        "Company Second", "QUALIFIED");

    repository.save(leadWithSameEmailFirst);
    repository.save(leadWithSameEmailSecond);

    Optional<LeadEntity> result = repository.findByEmail("same@example.com");

    assertThat(result).isPresent();
    assertThat(result.get().contact().email()).isEqualTo("same@example.com");
  }

  @Test
  void findByEmailShouldReturnEmptyWhenRepositoryIsEmpty() {
    Optional<LeadEntity> result = repository.findByEmail("any@example.com");

    assertThat(result).isEmpty();
  }

  @Test
  void findAllShouldReturnEmptyListWhenRepositoryIsEmpty() {
    List<LeadEntity> result = repository.findAll();

    assertThat(result).isEmpty();
  }

  @Test
  void findAllShouldReturnAllStoredLeads() {
    repository.save(leadFirst);
    repository.save(leadSecond);
    repository.save(leadThird);

    List<LeadEntity> result = repository.findAll();

    assertThat(result).hasSize(3);
    assertThat(result).containsExactlyInAnyOrder(leadFirst, leadSecond, leadThird);
  }

  @Test
  void findAllShouldReturnNewArrayListInstanceEachTime() {
    repository.save(leadFirst);

    List<LeadEntity> firstCall = repository.findAll();
    List<LeadEntity> secondCall = repository.findAll();

    assertThat(firstCall).isEqualTo(secondCall);
    assertThat(firstCall).isNotSameAs(secondCall);
  }
}