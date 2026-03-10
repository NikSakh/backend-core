package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  void shouldCreateLeadWhenValidData() {
    // Given
    UUID leadId = UUID.randomUUID();
    Lead lead = new Lead(leadId, "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // Then
    assertThat(lead.id()).isEqualTo(leadId);
    assertThat(lead.email()).isEqualTo("test@example.com");
    assertThat(lead.phone()).isEqualTo("+1234567890");
    assertThat(lead.company()).isEqualTo("ACME Inc");
    assertThat(lead.status()).isEqualTo("NEW");
  }

  @Test
  void shouldGenerateUniqueIdsWhenMultipleLeads() {
    // Given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    Lead lead1 = new Lead(id1, "client1@example.com", "+71111111111", "CompanyA", "NEW");
    Lead lead2 = new Lead(id2, "client2@example.com", "+72222222222", "CompanyB", "NEW");

    // Then
    assertThat(lead1.id()).isNotEqualTo(lead2.id());
  }

  @Test
  void shouldReturnIdWhenIdCalled() {
    // Given
    UUID leadId = UUID.randomUUID();
    Lead lead = new Lead(leadId, "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When
    UUID id = lead.id();

    // Then
    assertThat(id).isEqualTo(leadId);
  }

  @Test
  void shouldReturnEmailWhenEmailCalled() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When
    String email = lead.email();

    // Then
    assertThat(email).isEqualTo("test@example.com");
  }

  @Test
  void shouldReturnPhoneWhenPhoneCalled() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When
    String phone = lead.phone();

    // Then
    assertThat(phone).isEqualTo("+1234567890");
  }

  @Test
  void shouldReturnCompanyWhenCompanyCalled() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When
    String company = lead.company();

    // Then
    assertThat(company).isEqualTo("ACME Inc");
  }

  @Test
  void shouldReturnStatusWhenStatusCalled() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When
    String status = lead.status();

    // Then
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    // Given
    Lead lead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");

    // Then: Объект равен сам себе
    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    // Given
    UUID sameId = UUID.randomUUID();
    Lead firstLead = new Lead(sameId, "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    Lead secondLead = new Lead(sameId, "ivan@mail.ru", "+7123", "TechCorp", "NEW");

    // Then: Симметричность — порядок сравнения не важен
    assertThat(firstLead).isEqualTo(secondLead);
    assertThat(secondLead).isEqualTo(firstLead);
  }

  @Test
  void shouldPreventStringConfusionWhenUsingUUID() {
    // Given
    UUID leadId = UUID.randomUUID();
    Lead lead = new Lead(leadId, "test@example.com", "+1234567890", "ACME Inc", "NEW");

    // When & Then: проверяем типобезопасность — метод принимает только UUID
    findById(lead.id()); // OK: UUID to UUID — компилируется без ошибок

    // Попытка передать String вместо UUID вызовет ошибку компиляции
    // findById("some-string"); // ERROR: incompatible types — не скомпилируется
  }

  /**
   * Утилитный метод для демонстрации типобезопасности.
   * Принимает только UUID — попытка передать String приведёт к ошибке компиляции.
   *
   * @param id идентификатор лида типа UUID
   */
  private void findById(UUID id) {
    assertThat(id).isNotNull(); // Базовая проверка на null для демонстрации
  }
}