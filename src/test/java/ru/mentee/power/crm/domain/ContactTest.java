package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidDataProvided() {
    // Given
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";
    String phone = "+123456789";

    // When
    Contact contact = new Contact(email, phone, address);

    // Then
    assertThat(contact.email()).isEqualTo(email);
    assertThat(contact.phone()).isEqualTo(phone);
    assertThat(contact.address()).isEqualTo(address);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenEmailIsNullOrBlank(String invalidEmail) {
    // Given
    Address address = new Address("City", "Street", "12345");
    String phone = "+123456789";

    // When & Then
    assertThatThrownBy(() -> new Contact(invalidEmail, phone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenEmailContainsOnlyWhitespace() {
    // Given
    Address address = new Address("City", "Street", "12345");
    String phone = "+123456789";
    String whitespaceEmail = "   ";

    // When & Then
    assertThatThrownBy(() -> new Contact(whitespaceEmail, phone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email cannot be null or blank");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenPhoneIsNullOrBlank(String invalidPhone) {
    // Given
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";

    // When & Then
    assertThatThrownBy(() -> new Contact(email, invalidPhone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenPhoneContainsOnlyWhitespace() {
    // Given
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";
    String whitespacePhone = "   ";

    // When & Then
    assertThatThrownBy(() -> new Contact(email, whitespacePhone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenAddressIsNull() {
    // Given
    String email = "test@example.com";
    String phone = "+123456789";
    Address nullAddress = null;

    // When & Then
    assertThatThrownBy(() -> new Contact(email, phone, nullAddress))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address cannot be null");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    // Given
    Address address1 = new Address("City1", "Street1", "11111");
    Address address2 = new Address("City1", "Street1", "11111");

    Contact contact1 = new Contact("test1@example.com", "+111", address1);
    Contact contact2 = new Contact("test1@example.com", "+111", address2);

    // Then
    assertThat(contact1).isEqualTo(contact2);
    assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentEmail() {
    // Given
    Address address = new Address("City", "Street", "12345");

    Contact contact1 = new Contact("test1@example.com", "+123", address);
    Contact contact2 = new Contact("test2@example.com", "+123", address);

    // Then
    assertThat(contact1).isNotEqualTo(contact2);
  }

  @Test
  void shouldNotBeEqualWhenDifferentPhone() {
    // Given
    Address address = new Address("City", "Street", "12345");

    Contact contact1 = new Contact("test@example.com", "+123", address);
    Contact contact2 = new Contact("test@example.com", "+456", address);

    // Then
    assertThat(contact1).isNotEqualTo(contact2);
  }

  @Test
  void shouldNotBeEqualWhenDifferentAddress() {
    // Given
    Address address1 = new Address("City1", "Street1", "11111");
    Address address2 = new Address("City2", "Street2", "22222");

    Contact contact1 = new Contact("test@example.com", "+123", address1);
    Contact contact2 = new Contact("test@example.com", "+123", address2);

    // Then
    assertThat(contact1).isNotEqualTo(contact2);
  }

  @Test
  void shouldReturnCorrectToString() {
    // Given
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123", address);

    // When
    String toString = contact.toString();

    // Then
    assertThat(toString).contains("test@example.com");
    assertThat(toString).contains("+123");
    assertThat(toString).contains("City");
    assertThat(toString).contains("Street");
    assertThat(toString).contains("12345");
  }

  @Test
  void shouldHandleMinimalValidEmail() {
    // Given
    Address address = new Address("City", "Street", "12345");
    String minimalEmail = "a@b.c";

    // When
    Contact contact = new Contact(minimalEmail, "+123", address);

    // Then
    assertThat(contact.email()).isEqualTo(minimalEmail);
  }

  @Test
  void shouldHandleLongPhoneNumber() {
    // Given
    Address address = new Address("City", "Street", "12345");
    String longPhone = "+12345678901234567890";

    // When
    Contact contact = new Contact("test@example.com", longPhone, address);

    // Then
    assertThat(contact.phone()).isEqualTo(longPhone);
  }

  @Test
  void shouldDelegateToAddressFieldsCorrectly() {
    // Given
    Address address = new Address("Metropolis", "Main St", "90210");
    Contact contact = new Contact("delegate@example.com", "+999", address);

    // When & Then
    assertThat(contact.address().city()).isEqualTo("Metropolis");
    assertThat(contact.address().street()).isEqualTo("Main St");
    assertThat(contact.address().zip()).isEqualTo("90210");
  }

  @Test
  void shouldPreserveImmutabilityWhenReusingAddress() {
    // Given
    Address sharedAddress = new Address("Shared City", "Shared St", "55555");
    Contact contact1 = new Contact("first@example.com", "+111", sharedAddress);
    Contact contact2 = new Contact("second@example.com", "+222", sharedAddress);

    // Then: проверяем, что оба контакта используют один и тот же объект Address
    assertThat(contact1.address()).isSameAs(contact2.address());
  }
}