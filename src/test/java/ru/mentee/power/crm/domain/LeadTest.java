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
    LeadEntity lead = new LeadEntity(id, contact, "Acme Corp", "NEW");

    // Then
    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  void shouldAccessEmailThroughDelegationWhenLeadCreated() {
    // Given
    Address address = new Address("New York", "Broadway", "10001");
    Contact contact = new Contact("alice@example.com", "+987654321", address);
    LeadEntity lead = new LeadEntity(UUID.randomUUID(), contact, "Tech Inc", "QUALIFIED");

    // When & Then
    assertThat(lead.contact().email()).isEqualTo("alice@example.com");
    assertThat(lead.contact().address().city()).isEqualTo("New York");
  }

  @Test
  void shouldBeEqualWhenSameIdButDifferentContact() {
    // Given
    Address addressFirst = new Address("City1", "Street1", "11111");
    Address addressSecond = new Address("City2", "Street2", "22222");
    Contact contactFirst = new Contact("email1@example.com", "+111", addressFirst);
    Contact contactSecond = new Contact("email2@example.com", "+222", addressSecond);

    UUID sameId = UUID.randomUUID();
    LeadEntity leadFist = new LeadEntity(sameId, contactFirst, "CompanyA", "NEW");
    LeadEntity leadSecond = new LeadEntity(sameId, contactSecond, "CompanyB", "NEW");

    // Then
    assertThat(leadFist).isNotEqualTo(leadSecond);
    assertThat(leadFist.id()).isEqualTo(leadSecond.id());
  }

  @Test
  void shouldThrowExceptionWhenContactIsNull() {
    // When & Then
    assertThatThrownBy(() -> new LeadEntity(
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
    assertThatThrownBy(() -> new LeadEntity(
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
    // Given
    Address expectedAddress = new Address("Seattle", "Pike St", "98101");
    Contact expectedContact = new Contact("bob@example.com", "+555", expectedAddress);
    LeadEntity lead = new LeadEntity(UUID.randomUUID(), expectedContact, "Cloud Corp", "CONVERTED");

    // When
    Contact contact = lead.contact();
    Address address = contact.address();
    String city = address.city();

    String cityDirect = lead.contact().address().city();

    // Then
    assertThat(city).isEqualTo("Seattle");
    assertThat(cityDirect).isEqualTo("Seattle");
    assertThat(contact).isEqualTo(expectedContact);
    assertThat(address).isEqualTo(expectedAddress);
  }
}