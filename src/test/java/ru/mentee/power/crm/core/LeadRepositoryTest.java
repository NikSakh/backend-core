package ru.mentee.power.crm.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.LeadEntity;

class LeadRepositoryTest {

  @Test
  @DisplayName("Should automatically deduplicate leads by id")
  void shouldDeduplicateLeadsById() {
    LeadRepository repository = new LeadRepository();
    UUID leadId = UUID.randomUUID();
    Address address = new Address("123 Main St", "City", "12345");
    Contact contact = new Contact("John Doe", "john@example.com", address);
    LeadEntity lead = new LeadEntity(leadId, contact, "Acme Corp", "NEW");

    boolean firstAddResult = repository.add(lead);
    boolean secondAddResult = repository.add(lead);

    assertThat(firstAddResult).isTrue();
    assertThat(secondAddResult).isFalse();
    assertThat(repository.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("Should allow different leads with different ids")
  void shouldAllowDifferentLeads() {
    LeadRepository repository = new LeadRepository();
    UUID firstLeadId = UUID.randomUUID();
    UUID secondLeadId = UUID.randomUUID();

    Address firstAddress = new Address("456 Oak Ave", "Town", "67890");
    Contact firstContact = new Contact("Alice Smith", "alice@example.com", firstAddress);
    LeadEntity firstLead = new LeadEntity(firstLeadId, firstContact, "Evil corp", "NEW");

    Address secondAddress = new Address("789 Pine Rd", "Village", "54321");
    Contact secondContact = new Contact("Bob Johnson", "bob@example.com", secondAddress);
    LeadEntity secondLead = new LeadEntity(secondLeadId, secondContact, "Lol corp", "NEW");

    boolean firstAddResult = repository.add(firstLead);
    boolean secondAddResult = repository.add(secondLead);

    assertThat(firstAddResult).isTrue();
    assertThat(secondAddResult).isTrue();
    assertThat(repository.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should find existing lead through contains")
  void shouldFindExistingLead() {
    LeadRepository repository = new LeadRepository();
    UUID leadId = UUID.randomUUID();
    Address address = new Address("321 Elm St", "Metropolis", "11223");
    Contact contact = new Contact("Charlie Brown", "charlie@example.com", address);
    LeadEntity lead = new LeadEntity(leadId, contact, "Company corp", "NEW");
    repository.add(lead);

    boolean containsResult = repository.contains(lead);

    assertThat(containsResult).isTrue();
  }

  @Test
  @DisplayName("Should return unmodifiable set from findAll")
  void shouldReturnUnmodifiableSet() {
    LeadRepository repository = new LeadRepository();
    UUID leadId = UUID.randomUUID();
    Address address = new Address("999 Maple Dr", "Gotham", "99999");
    Contact contact = new Contact("Diana Prince", "diana@example.com", address);
    LeadEntity lead = new LeadEntity(leadId, contact, "Aboba corp", "NEW");
    repository.add(lead);

    Set<LeadEntity> resultSet = repository.findAll();

    assertThatThrownBy(() -> resultSet.add(new LeadEntity(
        UUID.randomUUID(),
        new Contact("Test", "test@example.com", new Address("Test", "Test", "00000")),
        "Test Company",
        "NEW"
    )))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should perform contains() faster than ArrayList")
  void shouldPerformFasterThanArrayList() {
    int numberOfLeads = 50_000;
    int numberOfChecks = 5_000;

    Set<LeadEntity> hashSet = new HashSet<>();
    List<LeadEntity> arrayList = new ArrayList<>();

    for (int i = 0; i < numberOfLeads; i++) {
      UUID leadId = UUID.randomUUID();
      Address address = new Address("Street " + i, "City " + i, String.format("%05d", i));
      Contact contact = new Contact("Name " + i, "email" + i + "@test.com", address);
      LeadEntity lead = new LeadEntity(leadId, contact, "Company " + i, "NEW");

      hashSet.add(lead);
      arrayList.add(lead);
    }

    LeadEntity targetLead = arrayList.get(numberOfLeads / 2);

    for (int i = 0; i < 100; i++) {
      hashSet.contains(targetLead);
      arrayList.contains(targetLead);
    }

    long hashSetStartTime = System.nanoTime();
    for (int i = 0; i < numberOfChecks; i++) {
      hashSet.contains(targetLead);
    }
    long hashSetDuration = System.nanoTime() - hashSetStartTime;

    long arrayListStartTime = System.nanoTime();
    for (int i = 0; i < numberOfChecks; i++) {
      arrayList.contains(targetLead);
    }
    long arrayListDuration = System.nanoTime() - arrayListStartTime;

    System.out.println("HashSet duration: " + hashSetDuration + " ns");
    System.out.println("ArrayList duration: " + arrayListDuration + " ns");
    double speedRatio = (double) arrayListDuration / hashSetDuration;
    System.out.println("Speed ratio: " + speedRatio + "x");

    String failMessage = String.format(
        ("HashSet должен быть быстрее ArrayList минимум в 10 раз, но соотношение: %.2fx"),
        speedRatio
    );

    assertThat(arrayListDuration)
        .withFailMessage(failMessage)
        .isGreaterThanOrEqualTo(hashSetDuration * 10);
  }
}