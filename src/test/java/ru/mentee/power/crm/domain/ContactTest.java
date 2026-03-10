package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    // Given
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+123456789", address);

    // Then
    assertThat(contact.address()).isEqualTo(address);
    assertThat(contact.address().city()).isEqualTo("San Francisco");
  }

  @Test
  void shouldDelegateToAddressWhenAccessingCity() {
    // Given
    Address expectedAddress = new Address("New York", "Broadway", "10001");
    Contact contact = new Contact("alice@example.com", "+987654321", expectedAddress);

    // Then
    assertThat(contact.address().city()).isEqualTo("New York");
    assertThat(contact.address().street()).isEqualTo("Broadway");
  }

  @Test
  void shouldThrowExceptionWhenAddressIsNull() {
    // When & Then
    assertThatThrownBy(() -> new Contact("test@example.com", "+123", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address cannot be null");
  }
}