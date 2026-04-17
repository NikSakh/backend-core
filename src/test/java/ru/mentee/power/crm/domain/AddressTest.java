package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void shouldCreateAddressWhenValidData() {
    Address address = new Address("San Francisco", "123 Main St", "94105");

    assertThat(address.city()).isEqualTo("San Francisco");
    assertThat(address.street()).isEqualTo("123 Main St");
    assertThat(address.zip()).isEqualTo("94105");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Address firstAddress = new Address("San Francisco", "123 Main St", "94105");
    Address secondAddress = new Address("San Francisco", "123 Main St", "94105");

    assertThat(firstAddress).isEqualTo(secondAddress);
    assertThat(firstAddress.hashCode()).isEqualTo(secondAddress.hashCode());
  }

  @Test
  void shouldThrowExceptionWhenCityIsNull() {
    assertThatThrownBy(() -> new Address(null, "123 Main St", "94105"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("City cannot be null or blank");
  }

  @Test
  void shouldThrowExceptionWhenZipIsBlank() {
    assertThatThrownBy(() -> new Address("San Francisco", "123 Main St", ""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ZIP code cannot be null or blank");
  }
}