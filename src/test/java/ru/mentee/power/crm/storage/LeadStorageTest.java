package ru.mentee.power.crm.storage;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LeadStorageTest {

  private LeadStorage storage;
  private UUID id1;
  private UUID id2;
  private UUID id3;

  @BeforeEach
  void setUp() {
    storage = new LeadStorage();
    id1 = UUID.randomUUID();
    id2 = UUID.randomUUID();
    id3 = UUID.randomUUID();
  }

  @Test
  void shouldAddLeadSuccessfullyWhenStorageIsEmpty() {
    // Given
    Lead lead = new Lead(id1, "client1@example.com", "+123", "CompanyA", "NEW");

    // When
    boolean result = storage.add(lead);

    // Then
    assertThat(result).isTrue();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(lead);
  }

  @Test
  void shouldAddMultipleLeadsSuccessfully() {
    // Given
    Lead lead1 = new Lead(id1, "client1@example.com", "+123", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "client2@example.com", "+456", "CompanyB", "NEW");
    Lead lead3 = new Lead(id3, "client3@example.com", "+789", "CompanyC", "NEW");

    // When
    boolean result1 = storage.add(lead1);
    boolean result2 = storage.add(lead2);
    boolean result3 = storage.add(lead3);

    // Then
    assertThat(result1).isTrue();
    assertThat(result2).isTrue();
    assertThat(result3).isTrue();
    assertThat(storage.size()).isEqualTo(3);
    assertThat(storage.findAll())
        .containsExactly(lead1, lead2, lead3);
  }

  @Test
  void shouldNotAddLeadWithDuplicateEmail() {
    // Given
    Lead lead1 = new Lead(id1, "duplicate@example.com", "+123", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "duplicate@example.com", "+456", "CompanyB", "NEW"); // Тот же email

    // When
    boolean result1 = storage.add(lead1);
    boolean result2 = storage.add(lead2);

    // Then
    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(lead1);
  }

  @Test
  void shouldReturnEmptyArrayWhenNoLeadsAdded() {
    // When
    Lead[] result = storage.findAll();

    // Then
    assertThat(result).isEmpty();
    assertThat(storage.size()).isZero();
  }

  @Test
  void shouldPreserveOrderOfAddedLeads() {
    // Given
    Lead lead1 = new Lead(id1, "first@example.com", "+111", "FirstCo", "NEW");
    Lead lead2 = new Lead(id2, "second@example.com", "+222", "SecondCo", "NEW");
    Lead lead3 = new Lead(id3, "third@example.com", "+333", "ThirdCo", "NEW");

    // When
    storage.add(lead1);
    storage.add(lead2);
    storage.add(lead3);

    // Then
    Lead[] allLeads = storage.findAll();
    assertThat(allLeads).containsExactly(lead1, lead2, lead3);
  }

  @Test
  void shouldHandleStorageFullException() {
    // Given: заполняем хранилище до предела
    for (int i = 0; i < 100; i++) {
      UUID id = UUID.randomUUID();
      String email = "client" + i + "@example.com";
      Lead lead = new Lead(id, email, "+123", "Company" + i, "NEW");
      storage.add(lead);
    }

    Lead newLead = new Lead(
        UUID.randomUUID(),
        "new@example.com",
        "+999",
        "NewCo",
        "NEW"
    );

    // When & Then
    assertThatThrownBy(() -> storage.add(newLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldCorrectlyCalculateSize() {
    // Given
    Lead lead1 = new Lead(id1, "client1@example.com", "+123", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "client2@example.com", "+456", "CompanyB", "NEW");

    // When
    storage.add(lead1);
    assertThat(storage.size()).isEqualTo(1);

    storage.add(lead2);
    assertThat(storage.size()).isEqualTo(2);
  }

  @Test
  void shouldFindAllLeadsCorrectly() {
    // Given
    Lead lead1 = new Lead(id1, "client1@example.com", "+123", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "client2@example.com", "+456", "CompanyB", "NEW");

    // When
    storage.add(lead1);
    storage.add(lead2);

    // Then
    Lead[] allLeads = storage.findAll();
    assertThat(allLeads)
        .hasSize(2)
        .contains(lead1, lead2);
  }

  @Test
  void shouldNotAddNullLead() {
    // When & Then: проверяем тип и сообщение исключения
    assertThatThrownBy(() -> storage.add(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Lead cannot be null");
  }

  @Test
  void shouldHandleMultipleDuplicateEmails() {
    // Given
    Lead lead1 = new Lead(id1, "test@example.com", "+123", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "another@example.com", "+456", "CompanyB", "NEW");
    Lead duplicate1 = new Lead(id3, "test@example.com", "+789", "CompanyC", "NEW"); // Дубликат lead1
    Lead duplicate2 = new Lead(
        UUID.randomUUID(),
        "another@example.com",
        "+000",
        "CompanyD",
        "NEW"
    ); // Дубликат lead2

    // When
    boolean result1 = storage.add(lead1);
    boolean result2 = storage.add(lead2);
    boolean result3 = storage.add(duplicate1);
    boolean result4 = storage.add(duplicate2);

    // Then
    assertThat(result1).isTrue();
    assertThat(result2).isTrue();
    assertThat(result3).isFalse();
    assertThat(result4).isFalse();
    assertThat(storage.size()).isEqualTo(2);
    assertThat(storage.findAll())
        .containsExactly(lead1, lead2);
  }
}