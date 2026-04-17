package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidDataProvided() {
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";
    String phone = "+123456789";

    Contact contact = new Contact(email, phone, address);

    assertThat(contact.email()).isEqualTo(email);
    assertThat(contact.phone()).isEqualTo(phone);
    assertThat(contact.address()).isEqualTo(address);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenEmailIsNullOrBlank(String invalidEmail) {
    Address address = new Address("City", "Street", "12345");
    String phone = "+123456789";

    assertThatThrownBy(() -> new Contact(invalidEmail, phone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenEmailContainsOnlyWhitespace() {
    Address address = new Address("City", "Street", "12345");
    String phone = "+123456789";
    String whitespaceEmail = "   ";

    assertThatThrownBy(() -> new Contact(whitespaceEmail, phone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Email cannot be null or blank");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowExceptionWhenPhoneIsNullOrBlank(String invalidPhone) {
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";

    assertThatThrownBy(() -> new Contact(email, invalidPhone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenPhoneContainsOnlyWhitespace() {
    Address address = new Address("City", "Street", "12345");
    String email = "test@example.com";
    String whitespacePhone = "   ";

    assertThatThrownBy(() -> new Contact(email, whitespacePhone, address))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Phone cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenAddressIsNull() {
    String email = "test@example.com";
    String phone = "+123456789";
    Address nullAddress = null;

    assertThatThrownBy(() -> new Contact(email, phone, nullAddress))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address cannot be null");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Address addressFirst = new Address("City1", "Street1", "11111");
    Address addressSecond = new Address("City1", "Street1", "11111");

    Contact contactFirst = new Contact("test1@example.com", "+111", addressFirst);
    Contact contactSecond = new Contact("test1@example.com", "+111", addressSecond);

    assertThat(contactFirst).isEqualTo(contactSecond);
    assertThat(contactFirst.hashCode()).isEqualTo(contactSecond.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentEmail() {
    Address address = new Address("City", "Street", "12345");

    Contact contactFirst = new Contact("test1@example.com", "+123", address);
    Contact contactSecond = new Contact("test2@example.com", "+123", address);

    assertThat(contactFirst).isNotEqualTo(contactSecond);
  }

  @Test
  void shouldNotBeEqualWhenDifferentPhone() {
    Address address = new Address("City", "Street", "12345");

    Contact contactFirst = new Contact("test@example.com", "+123", address);
    Contact contactSecond = new Contact("test@example.com", "+456", address);

    assertThat(contactFirst).isNotEqualTo(contactSecond);
  }

  @Test
  void shouldNotBeEqualWhenDifferentAddress() {
    Address addressFirst = new Address("City1", "Street1", "11111");
    Address addressSecond = new Address("City2", "Street2", "22222");

    Contact contactFirst = new Contact("test@example.com", "+123", addressFirst);
    Contact contactSecond = new Contact("test@example.com", "+123", addressSecond);

    assertThat(contactFirst).isNotEqualTo(contactSecond);
  }

  @Test
  void shouldReturnCorrectToString() {
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123", address);

    String toString = contact.toString();

    assertThat(toString).contains("test@example.com");
    assertThat(toString).contains("+123");
    assertThat(toString).contains("City");
    assertThat(toString).contains("Street");
    assertThat(toString).contains("12345");
  }

  @Test
  void shouldHandleMinimalValidEmail() {
    Address address = new Address("City", "Street", "12345");
    String minimalEmail = "a@b.c";

    Contact contact = new Contact(minimalEmail, "+123", address);

    assertThat(contact.email()).isEqualTo(minimalEmail);
  }

  @Test
  void shouldHandleLongPhoneNumber() {
    Address address = new Address("City", "Street", "12345");
    String longPhone = "+12345678901234567890";

    Contact contact = new Contact("test@example.com", longPhone, address);

    assertThat(contact.phone()).isEqualTo(longPhone);
  }

  @Test
  void shouldDelegateToAddressFieldsCorrectly() {
    Address address = new Address("Metropolis", "Main St", "90210");
    Contact contact = new Contact("delegate@example.com", "+999", address);

    assertThat(contact.address().city()).isEqualTo("Metropolis");
    assertThat(contact.address().street()).isEqualTo("Main St");
    assertThat(contact.address().zip()).isEqualTo("90210");
  }

  @Test
  void shouldPreserveImmutabilityWhenReusingAddress() {
    Address sharedAddress = new Address("Shared City", "Shared St", "55555");
    Contact contactFirst = new Contact("first@example.com", "+111", sharedAddress);
    Contact contactSecond = new Contact("second@example.com", "+222", sharedAddress);

    assertThat(contactFirst.address()).isSameAs(contactSecond.address());
  }
}