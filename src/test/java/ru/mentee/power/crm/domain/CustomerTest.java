package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void shouldReuseContactWhenCreatingCustomer() {
    // Given
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "456 Oak Ave", "90210");

    Contact contact = new Contact("john@example.com", "+123456789", contactAddress);

    Customer customer = new Customer(
        UUID.randomUUID(),
        contact,
        billingAddress,
        "GOLD"
    );

    // Then
    assertThat(customer.contact().address())
        .isNotEqualTo(customer.billingAddress());

    assertThat(customer.contact().email()).isEqualTo("john@example.com");
    assertThat(customer.contact().address().city()).isEqualTo("San Francisco");
    assertThat(customer.billingAddress().city()).isEqualTo("Los Angeles");
    assertThat(customer.loyaltyTier()).isEqualTo("GOLD");
  }

  @Test
  void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
    // Given
    Address sharedAddress = new Address("New York", "Broadway", "10001");
    Contact sharedContact = new Contact("shared@example.com", "+987654321", sharedAddress);

    UUID id = UUID.randomUUID();

    Lead lead = new Lead(
        id,
        sharedContact,
        "Acme Corp",
        "NEW"
    );

    Customer customer = new Customer(
        id,
        sharedContact,
        new Address("Boston", "Park St", "02108"),
        "SILVER"
    );

    assertThat(lead.contact()).isSameAs(customer.contact());

    assertThat(lead.contact().email()).isEqualTo(customer.contact().email());
    assertThat(lead.contact().phone()).isEqualTo(customer.contact().phone());
    assertThat(lead.contact().address()).isSameAs(customer.contact().address());

    assertThat(lead.company()).isEqualTo("Acme Corp");
    assertThat(lead.status()).isEqualTo("NEW");

    assertThat(customer.loyaltyTier()).isEqualTo("SILVER");
    assertThat(customer.billingAddress().city()).isEqualTo("Boston");
  }
}