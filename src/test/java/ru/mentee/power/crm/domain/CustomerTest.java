package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void shouldReuseContactWhenCreatingCustomer() {
    // Given: создаём Contact и два разных Address
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "456 Oak Ave", "90210");

    Contact contact = new Contact("john@example.com", "+123456789", contactAddress);

    Customer customer = new Customer(
        UUID.randomUUID(),
        contact,
        billingAddress,
        "GOLD"
    );

    // Then: проверяем, что адреса разные (разные объекты)
    assertThat(customer.contact().address())
        .isNotEqualTo(customer.billingAddress());

    // Дополнительные проверки корректности данных
    assertThat(customer.contact().email()).isEqualTo("john@example.com");
    assertThat(customer.contact().address().city()).isEqualTo("San Francisco");
    assertThat(customer.billingAddress().city()).isEqualTo("Los Angeles");
    assertThat(customer.loyaltyTier()).isEqualTo("GOLD");
  }

  @Test
  void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
    // Given: создаём одинаковый Contact для использования в Lead и Customer
    Address sharedAddress = new Address("New York", "Broadway", "10001");
    Contact sharedContact = new Contact("shared@example.com", "+987654321", sharedAddress);

    UUID id = UUID.randomUUID();

    // Создаём Lead с общим Contact
    Lead lead = new Lead(
        id,
        sharedContact,
        "Acme Corp",
        "NEW"
    );

    // Создаём Customer с тем же самым Contact
    Customer customer = new Customer(
        id,
        sharedContact,
        new Address("Boston", "Park St", "02108"),
        "SILVER"
    );

    // Then: демонстрируем переиспользование Contact без дублирования
    // Проверяем, что это один и тот же объект Contact
    assertThat(lead.contact()).isSameAs(customer.contact());

    // Проверяем, что данные Contact идентичны в обоих объектах
    assertThat(lead.contact().email()).isEqualTo(customer.contact().email());
    assertThat(lead.contact().phone()).isEqualTo(customer.contact().phone());
    assertThat(lead.contact().address()).isSameAs(customer.contact().address());

    // Проверяем корректность специфичных для Lead полей
    assertThat(lead.company()).isEqualTo("Acme Corp");
    assertThat(lead.status()).isEqualTo("NEW");

    // Проверяем корректность специфичных для Customer полей
    assertThat(customer.loyaltyTier()).isEqualTo("SILVER");
    assertThat(customer.billingAddress().city()).isEqualTo("Boston");
  }
}