package ru.mentee.power.crm.domain;

import java.util.Set;
import java.util.UUID;

/**
 * Value object representing a customer in the CRM system.
 * Demonstrates reuse of the Contact class across different domain entities.
 */
public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {

  /**
   * Set of allowed loyalty tiers.
   */
  private static final Set<String> ALLOWED_LOYALTY_TIERS = Set.of(
      "BRONZE", "SILVER", "GOLD"
  );

  /**
   * Compact constructor that validates:
   * - id, contact, billingAddress are not null;
   * - loyaltyTier is not null/blank and is one of the allowed values.
   */
  public Customer {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact cannot be null");
    }
    if (billingAddress == null) {
      throw new IllegalArgumentException("Billing address cannot be null");
    }
    if (loyaltyTier == null || loyaltyTier.isBlank()) {
      throw new IllegalArgumentException("Loyalty tier cannot be null or blank");
    }
    if (!ALLOWED_LOYALTY_TIERS.contains(loyaltyTier)) {
      throw new IllegalArgumentException(
          "Loyalty tier must be one of: " + ALLOWED_LOYALTY_TIERS + ", but was: '" + loyaltyTier
              + "'"
      );
    }
  }
}