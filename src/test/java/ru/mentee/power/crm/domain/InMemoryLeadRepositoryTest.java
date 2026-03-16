package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLeadRepositoryTest {

  @Test
  void shouldAddLeadAndFindWhenRepositoryIsEmpty() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID id = UUID.randomUUID();
    Lead firstLead = createTestLead(id);

    // When
    boolean added = repository.add(firstLead);

    // Then
    assertThat(added).isTrue();
    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findById(id)).contains(firstLead);
  }

  @Test
  void shouldReturnEmptyOptionalWhenLeadNotFound() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    populateRepositoryWithTenLeads(repository);
    UUID nonExistentId = UUID.randomUUID();

    // When
    Optional<Lead> result = repository.findById(nonExistentId);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  void shouldRejectDuplicateLead() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID sameId = UUID.randomUUID();
    Lead existingLead = createTestLead(sameId);
    Lead duplicateLead = createTestLead(sameId);

    // When
    boolean firstAdded = repository.add(existingLead);
    boolean secondAdded = repository.add(duplicateLead);

    // Then
    assertThat(firstAdded).isTrue();
    assertThat(secondAdded).isFalse();
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  void shouldRemoveExistingLead() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    UUID leadToRemoveId = UUID.randomUUID();
    Lead leadToRemove = createTestLead(leadToRemoveId);
    repository.add(leadToRemove);
    repository.add(createTestLead(UUID.randomUUID())); // добавляем ещё один для полноты теста

    // When
    boolean removed = repository.remove(leadToRemoveId);

    // Then
    assertThat(removed).isTrue();
    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findById(leadToRemoveId)).isEmpty();
  }

  @Test
  void shouldProtectInternalStorageWithDefensiveCopy() {
    // Given
    InMemoryLeadRepository repository = new InMemoryLeadRepository();
    repository.add(createTestLead(UUID.randomUUID()));
    repository.add(createTestLead(UUID.randomUUID()));

    // When
    List<Lead> clientList = repository.findAll();
    clientList.clear();

    // Then
    assertThat(repository.findAll()).hasSize(2);
  }

  private Lead createTestLead(UUID id) {
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123", address);
    return new Lead(id, contact, "Test Company", "NEW");
  }

  private void populateRepositoryWithTenLeads(InMemoryLeadRepository repository) {
    for (int i = 0; i < 10; i++) {
      repository.add(createTestLead(UUID.randomUUID()));
    }
  }
}