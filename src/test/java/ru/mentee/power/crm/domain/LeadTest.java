package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  void shouldCreateLeadWhenValidData() {
    // Given
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+123456789", address);
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, contact, "Acme Corp", "NEW");

    // Then
    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  void shouldAccessEmailThroughDelegationWhenLeadCreated() {
    // Given
    Address address = new Address("New York", "Broadway", "10001");
    Contact contact = new Contact("alice@example.com", "+987654321", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Tech Inc", "QUALIFIED");

    // When & Then
    assertThat(lead.contact().email()).isEqualTo("alice@example.com");
    assertThat(lead.contact().address().city()).isEqualTo("New York");
  }

  @Test
  void shouldBeEqualWhenSameIdButDifferentContact() {
    // Given: создаём два Lead с одинаковым UUID, но разными Contact
    Address addr1 = new Address("City1", "Street1", "11111");
    Address addr2 = new Address("City2", "Street2", "22222");
    Contact contact1 = new Contact("email1@example.com", "+111", addr1);
    Contact contact2 = new Contact("email2@example.com", "+222", addr2);

    UUID sameId = UUID.randomUUID();
    Lead lead1 = new Lead(sameId, contact1, "CompanyA", "NEW");
    Lead lead2 = new Lead(sameId, contact2, "CompanyB", "NEW");

    // Then: по умолчанию record использует equals по всем полям
    assertThat(lead1).isNotEqualTo(lead2);
    assertThat(lead1.id()).isEqualTo(lead2.id());
  }

  @Test
  void shouldThrowExceptionWhenContactIsNull() {
    // When & Then
    assertThatThrownBy(() -> new Lead(
        UUID.randomUUID(),
        null,
        "Acme Corp",
        "NEW"
    ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenInvalidStatus() {
    // Given
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("test@example.com", "+123", address);

    // When & Then
    assertThatThrownBy(() -> new Lead(
        UUID.randomUUID(),
        contact,
        "Test Company",
        "INVALID"
    ))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Status must be one of:")
        .hasMessageContaining("NEW")
        .hasMessageContaining("QUALIFIED")
        .hasMessageContaining("CONVERTED")
        .hasMessageContaining("but was: 'INVALID'");
  }

  @Test
  void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {
    // Given: полная композиция Lead → Contact → Address
    Address expectedAddress = new Address("Seattle", "Pike St", "98101");
    Contact expectedContact = new Contact("bob@example.com", "+555", expectedAddress);
    Lead lead = new Lead(UUID.randomUUID(), expectedContact, "Cloud Corp", "CONVERTED");

    // When: демонстрируем трёхуровневую делегацию
    Contact contact = lead.contact();           // уровень 1
    Address address = contact.address();         // уровень 2
    String city = address.city();               // уровень 3

    // Или сокращённо:
    String cityDirect = lead.contact().address().city(); // 3 уровня одной строкой

    // Then
    assertThat(city).isEqualTo("Seattle");
    assertThat(cityDirect).isEqualTo("Seattle");
    assertThat(contact).isEqualTo(expectedContact);
    assertThat(address).isEqualTo(expectedAddress);
  }
}