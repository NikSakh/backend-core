package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    // Given
    Contact contact = new Contact("John", "Doe", "john@example.com");

    // Then
    assertThat(contact.firstName()).isEqualTo("John");
    assertThat(contact.lastName()).isEqualTo("Doe");
    assertThat(contact.email()).isEqualTo("john@example.com");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    // Given
    Contact contact1 = new Contact("John", "Doe", "john@example.com");
    Contact contact2 = new Contact("John", "Doe", "john@example.com");

    // Then
    assertThat(contact1).isEqualTo(contact2);
    assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentData() {
    // Given
    Contact contact1 = new Contact("John", "Doe", "john@example.com");
    Contact contact2 = new Contact("Jane", "Smith", "jane@example.com");

    // Then
    assertThat(contact1).isNotEqualTo(contact2);
  }
}