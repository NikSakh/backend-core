package ru.mentee.power.crm.domain;

import java.util.Set;
import java.util.UUID;

public record LeadEntity(UUID id, Contact contact, String company, String status) {

  private static final Set<String> ALLOWED_STATUSES = Set.of(
      "NEW", "QUALIFIED", "CONVERTED"
  );

  public LeadEntity {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact cannot be null");
    }
    if (status == null || status.isBlank()) {
      throw new IllegalArgumentException("Status cannot be null or blank");
    }
    if (!ALLOWED_STATUSES.contains(status)) {
      throw new IllegalArgumentException(
          "Status must be one of: " + ALLOWED_STATUSES + ", but was: '" + status + "'"
      );
    }
  }
}