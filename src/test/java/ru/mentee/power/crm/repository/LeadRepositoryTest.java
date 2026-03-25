package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;

class LeadRepositoryTest {
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
  }

  @Test
  void shouldSaveAndFindLeadByIdWhenLeadSaved() {
    String leadId = "lead-1";
    Lead lead = new Lead(leadId, "john@example.com", "+79991234567", "Company A", "NEW");

    repository.save(lead);

    Lead foundLead = repository.findById(leadId);
    assertThat(foundLead).isNotNull();
    assertThat(foundLead.id()).isEqualTo(leadId);
    assertThat(foundLead.email()).isEqualTo("john@example.com");
    assertThat(foundLead.company()).isEqualTo("Company A");
    assertThat(foundLead.status()).isEqualTo("NEW");
  }

  @Test
  void shouldReturnNullWhenLeadNotFound() {
    String unknownId = "unknown-id";
    Lead foundLead = repository.findById(unknownId);

    assertThat(foundLead).isNull();
  }

  @Test
  void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
    Lead lead1 = new Lead("lead-1", "alice@example.com", "+79991111111",
        "Company 1", "NEW");
    Lead lead2 = new Lead("lead-2", "bob@example.com", "+79992222222",
        "Company 2", "QUALIFIED");
    Lead lead3 = new Lead("lead-3", "charlie@example.com", "+79993333333",
        "Company 3", "CONVERTED");

    repository.save(lead1);
    repository.save(lead2);
    repository.save(lead3);

    List<Lead> allLeads = repository.findAll();

    assertThat(allLeads).hasSize(3);
    assertThat(allLeads).containsExactlyInAnyOrder(lead1, lead2, lead3);
  }

  @Test
  void shouldDeleteLeadWhenLeadExists() {
    String leadId = "lead-1";
    Lead lead = new Lead(leadId, "test@example.com",
        "+79994444444", "Test Company", "NEW");
    repository.save(lead);

    repository.delete(leadId);

    Lead foundLead = repository.findById(leadId);
    assertThat(foundLead).isNull();
    assertThat(repository.size()).isEqualTo(0);
  }

  @Test
  void shouldOverwriteLeadWhenSaveWithSameId() {
    String sameId = "lead-1";
    Lead originalLead = new Lead(sameId, "original@example.com",
        "+79995555555", "Original Company", "NEW");
    repository.save(originalLead);

    Lead updatedLead = new Lead(sameId, "updated@example.com",
        "+79996666666", "Updated Company", "QUALIFIED");
    repository.save(updatedLead);

    Lead foundLead = repository.findById(sameId);
    assertThat(foundLead).isNotNull();
    assertThat(foundLead.email()).isEqualTo("updated@example.com");
    assertThat(foundLead.phone()).isEqualTo("+79996666666");
    assertThat(foundLead.company()).isEqualTo("Updated Company");
    assertThat(foundLead.status()).isEqualTo("QUALIFIED");
    assertThat(repository.size()).isEqualTo(1);
  }

  @Test
  void shouldFindFasterWithMapThanWithListFilter() {
    List<Lead> leadList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      String id = "lead-" + i;
      String email = "email" + i + "@test.com";
      String phone = "+7" + i;
      String company = "Company" + i;
      String status = "NEW";

      Lead lead = new Lead(id, email, phone, company, status);
      repository.save(lead);
      leadList.add(lead);
    }

    String targetId = "lead-500";

    long mapStart = System.nanoTime();
    Lead foundInMap = repository.findById(targetId);
    long mapDuration = System.nanoTime() - mapStart;

    long listStart = System.nanoTime();
    Lead foundInList = leadList.stream()
        .filter(lead -> lead.id().equals(targetId))
        .findFirst()
        .orElse(null);
    long listDuration = System.nanoTime() - listStart;

    assertThat(foundInMap).isEqualTo(foundInList);
    assertThat(listDuration).isGreaterThan(mapDuration * 10);

    System.out.println("Map поиск: " + mapDuration + " ns");
    System.out.println("List поиск: " + listDuration + " ns");
    System.out.println("Ускорение: " + (listDuration / (double) mapDuration) + "x");
  }

  @Test
  void shouldSaveBothLeadsEvenWithSameEmailAndPhoneBecauseRepositoryDoesNotCheckBusinessRules() {
    String email = "ivan@mail.ru";
    String phone = "+79001234567";

    String originalId = UUID.randomUUID().toString();
    Lead originalLead = new Lead(
        originalId,
        email,
        phone,
        "Acme Corp",
        "NEW"
    );

    String duplicateId = UUID.randomUUID().toString();
    Lead duplicateLead = new Lead(
        duplicateId,
        email,
        phone,
        "TechCorp",
        "HOT"
    );

    repository.save(originalLead);
    repository.save(duplicateLead);

    assertThat(repository.size()).isEqualTo(2);

    Lead foundOriginal = repository.findById(originalId);
    Lead foundDuplicate = repository.findById(duplicateId);

    assertThat(foundOriginal).isNotNull();
    assertThat(foundDuplicate).isNotNull();

    assertThat(foundOriginal.email()).isEqualTo(email);
    assertThat(foundOriginal.phone()).isEqualTo(phone);
    assertThat(foundDuplicate.email()).isEqualTo(email);
    assertThat(foundDuplicate.phone()).isEqualTo(phone);

    assertThat(foundOriginal.company()).isEqualTo("Acme Corp");
    assertThat(foundDuplicate.company()).isEqualTo("TechCorp");
    assertThat(foundOriginal.status()).isEqualTo("NEW");
    assertThat(foundDuplicate.status()).isEqualTo("HOT");
  }
}