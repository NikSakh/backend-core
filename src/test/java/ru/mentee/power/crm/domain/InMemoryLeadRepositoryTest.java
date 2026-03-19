package ru.mentee.power.crm.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryLeadRepositoryTest {

  private InMemoryLeadRepository repository;
  private UUID testId;
  private Lead testLead;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
    testId = UUID.randomUUID();
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123", address);
    testLead = new Lead(testId, contact, "Test Company", "NEW");
  }

  @Test
  @DisplayName("Should add lead when repository is empty")
  void shouldAddLeadWhenRepositoryIsEmpty() {
    boolean added = repository.add(testLead);
    assertThat(added).isTrue();
    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findById(testId)).contains(testLead);
  }

  @Test
  @DisplayName("Should reject null lead")
  void shouldRejectNullLead() {
    assertThatThrownBy(() -> repository.add(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Lead cannot be null");
  }

  @Test
  @DisplayName("Should reject duplicate lead")
  void shouldRejectDuplicateLead() {
    repository.add(testLead);
    boolean secondAdded = repository.add(testLead);
    assertThat(secondAdded).isFalse();
    assertThat(repository.findAll()).hasSize(1);
  }

  @Test
  @DisplayName("Should add multiple different leads")
  void shouldAddMultipleDifferentLeads() {
    UUID secondId = UUID.randomUUID();
    Address secondAddress = new Address("Another City", "Another Street", "54321");
    Contact secondContact = new Contact("another@example.com", "+456", secondAddress);
    Lead secondLead = new Lead(secondId, secondContact, "Another Company", "NEW");

    boolean firstAdded = repository.add(testLead);
    boolean secondAdded = repository.add(secondLead);

    assertThat(firstAdded).isTrue();
    assertThat(secondAdded).isTrue();
    assertThat(repository.findAll()).hasSize(2);
    assertThat(repository.findById(testId)).contains(testLead);
    assertThat(repository.findById(secondId)).contains(secondLead);
  }

  @Test
  @DisplayName("Should remove existing lead")
  void shouldRemoveExistingLead() {
    repository.add(testLead);
    boolean removed = repository.remove(testId);
    assertThat(removed).isTrue();
    assertThat(repository.findAll()).hasSize(0);
    assertThat(repository.findById(testId)).isEmpty();
  }

  @Test
  @DisplayName("Should not remove non-existent lead")
  void shouldNotRemoveNonExistentLead() {
    UUID nonExistentId = UUID.randomUUID();
    boolean removed = repository.remove(nonExistentId);
    assertThat(removed).isFalse();
  }

  @Test
  @DisplayName("Should reject null ID for removal")
  void shouldRejectNullIdForRemoval() {
    assertThatThrownBy(() -> repository.remove(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ID cannot be null");
  }

  @Test
  @DisplayName("Should remove lead and keep others")
  void shouldRemoveLeadAndKeepOthers() {
    UUID secondId = UUID.randomUUID();
    Address secondAddress = new Address("Another City", "Another Street", "54321");
    Contact secondContact = new Contact("another@example.com", "+456", secondAddress);
    Lead secondLead = new Lead(secondId, secondContact, "Another Company", "NEW");
    repository.add(testLead);
    repository.add(secondLead);

    boolean removed = repository.remove(testId);

    assertThat(removed).isTrue();
    assertThat(repository.findAll()).hasSize(1);
    assertThat(repository.findById(testId)).isEmpty();
    assertThat(repository.findById(secondId)).contains(secondLead);
  }

  @Test
  @DisplayName("Should find existing lead by ID")
  void shouldFindExistingLeadById() {
    repository.add(testLead);
    Optional<Lead> result = repository.findById(testId);
    assertThat(result).contains(testLead);
  }

  @Test
  @DisplayName("Should return empty optional for non-existent ID")
  void shouldReturnEmptyOptionalForNonExistentId() {
    UUID nonExistentId = UUID.randomUUID();
    Optional<Lead> result = repository.findById(nonExistentId);
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should reject null ID for findById")
  void shouldRejectNullIdForFindById() {
    assertThatThrownBy(() -> repository.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ID cannot be null");
  }

  @Test
  @DisplayName("Should return defensive copy of all leads")
  void shouldReturnDefensiveCopyOfAllLeads() {
    repository.add(testLead);
    UUID secondId = UUID.randomUUID();
    Address secondAddress = new Address("Another City", "Another Street", "54321");
    Contact secondContact = new Contact("another@example.com", "+456", secondAddress);
    Lead secondLead = new Lead(secondId, secondContact, "Another Company", "NEW");
    repository.add(secondLead);

    List<Lead> clientList = repository.findAll();
    clientList.clear();

    assertThat(repository.findAll()).hasSize(2);
  }

  @Test
  @DisplayName("Should return empty list when repository is empty")
  void shouldReturnEmptyListWhenRepositoryIsEmpty() {
    List<Lead> allLeads = repository.findAll();
    assertThat(allLeads).isEmpty();
  }

  @Test
  @DisplayName("Should return all leads when multiple exist")
  void shouldReturnAllLeadsWhenMultipleExist() {
    UUID secondId = UUID.randomUUID();
    Address secondAddress = new Address("Another City", "Another Street", "54321");
    Contact secondContact = new Contact("another@example.com", "+456", secondAddress);
    Lead secondLead = new Lead(secondId, secondContact, "Another Company", "NEW");
    repository.add(testLead);
    repository.add(secondLead);

    List<Lead> allLeads = repository.findAll();

    assertThat(allLeads).hasSize(2);
    assertThat(allLeads).containsExactlyInAnyOrder(testLead, secondLead);
  }

  @Test
  @DisplayName("Should handle sequential operations correctly")
  void shouldHandleSequentialOperationsCorrectly() {
    UUID secondId = UUID.randomUUID();
    Address secondAddress = new Address("Another City", "Another Street", "54321");
    Contact secondContact = new Contact("another@example.com", "+456", secondAddress);
    Lead secondLead = new Lead(secondId, secondContact, "Another Company", "NEW");

    repository.add(testLead);
    repository.add(secondLead);

    boolean firstRemove = repository.remove(testId);
    Optional<Lead> foundAfterRemove = repository.findById(testId);
    boolean secondAdd = repository.add(testLead);
    List<Lead> allAfterSequence = repository.findAll();

    assertThat(firstRemove).isTrue();
    assertThat(foundAfterRemove).isEmpty();
    assertThat(secondAdd).isTrue();
    assertThat(allAfterSequence).hasSize(2);
    assertThat(allAfterSequence).containsExactlyInAnyOrder(testLead, secondLead);
  }

  @Test
  @DisplayName("Should handle modifications safely without ConcurrentModificationException")
  void shouldHandleModificationsSafelyWithoutConcurrentModificationException() {
    // Given - populate with multiple leads
    for (int i = 0; i < 5; i++) {
      UUID id = UUID.randomUUID();
      Address address = new Address("City " + i, "Street " + i, String.format("%05d", i));
      Contact contact = new Contact("email" + i + "@test.com", "+123" + i, address);
      Lead lead = new Lead(id, contact, "Company " + i, "NEW");
      repository.add(lead);
    }

    int initialSize = repository.findAll().size();

    // When - perform a series of safe operations (remove and add back)
    UUID firstId = repository.findAll().get(0).id();
    boolean removed = repository.remove(firstId);

    // Create a new lead to add (not the same instance, to avoid potential issues)
    UUID newId = UUID.randomUUID();
    Address newAddress = new Address("New City", "New Street", "99999");
    Contact newContact = new Contact("new@test.com", "+999", newAddress);
    Lead newLead = new Lead(newId, newContact, "New Company", "NEW");
    boolean added = repository.add(newLead);

    // Then - verify consistency
    assertThat(removed).isTrue();
    assertThat(added).isTrue();
    assertThat(repository.findAll()).hasSize(initialSize); // size remains the same (1 removed, 1 added)
  }

  @Test
  @DisplayName("Should maintain consistency after multiple operations")
  void shouldMaintainConsistencyAfterMultipleOperations() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();

    Address address1 = new Address("City1", "Street1", "11111");
    Contact contact1 = new Contact("one@test.com", "+111", address1);
    Lead lead1 = new Lead(id1, contact1, "Company1", "NEW");

    Address address2 = new Address("City2", "Street2", "22222");
    Contact contact2 = new Contact("two@test.com", "+222", address2);
    Lead lead2 = new Lead(id2, contact2, "Company2", "NEW");


    Address address3 = new Address("City3", "Street3", "33333");
    Contact contact3 = new Contact("three@test.com", "+333", address3);
    Lead lead3 = new Lead(id3, contact3, "Company3", "NEW");

    repository.add(lead1);
    repository.add(lead2);
    repository.add(lead3);

    boolean remove1 = repository.remove(id1);
    boolean add1Again = repository.add(lead1);
    Optional<Lead> find2 = repository.findById(id2);
    List<Lead> all = repository.findAll();

    assertThat(remove1).isTrue();
    assertThat(add1Again).isTrue();
    assertThat(find2).contains(lead2);
    assertThat(all).hasSize(3);
    assertThat(all).containsExactlyInAnyOrder(lead1, lead2, lead3);
  }

  @Test
  @DisplayName("Should correctly handle edge case with single element")
  void shouldCorrectlyHandleEdgeCaseWithSingleElement() {
    repository.add(testLead);

    Optional<Lead> found = repository.findById(testId);
    boolean removed = repository.remove(testId);
    List<Lead> allAfterRemove = repository.findAll();
    boolean readded = repository.add(testLead);
    List<Lead> allAfterReadd = repository.findAll();

    assertThat(found).contains(testLead);
    assertThat(removed).isTrue();
    assertThat(allAfterRemove).isEmpty();
    assertThat(readded).isTrue();
    assertThat(allAfterReadd).hasSize(1);
    assertThat(allAfterReadd.get(0)).isEqualTo(testLead);
  }

  @Test
  @DisplayName("Should preserve order of elements as added")
  void shouldPreserveOrderOfElementsAsAdded() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();

    Address address1 = new Address("First City", "First Street", "11111");
    Contact contact1 = new Contact("first@test.com", "+111", address1);
    Lead lead1 = new Lead(id1, contact1, "First Company", "NEW");

    Address address2 = new Address("Second City", "Second Street", "22222");
    Contact contact2 = new Contact("second@test.com", "+222", address2);
    Lead lead2 = new Lead(id2, contact2, "Second Company", "NEW");

    Address address3 = new Address("Third City", "Third Street", "33333");
    Contact contact3 = new Contact("third@test.com", "+333", address3);
    Lead lead3 = new Lead(id3, contact3, "Third Company", "NEW");

    repository.add(lead1);
    repository.add(lead2);
    repository.add(lead3);

    List<Lead> allLeads = repository.findAll();

    assertThat(allLeads).hasSize(3);
    assertThat(allLeads.get(0)).isEqualTo(lead1);
    assertThat(allLeads.get(1)).isEqualTo(lead2);
    assertThat(allLeads.get(2)).isEqualTo(lead3);
  }
}