package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.UUID;

public record Lead(
    UUID id,
    String email,
    String phone,
    String company,
    String status
) {
  public Lead(UUID id, String email, String phone, String company, String status) {
    Objects.requireNonNull(id, "ID cannot be null");
    Objects.requireNonNull(email, "Email cannot be null");

    if (email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be blank");
    }

    this.id = id;
    this.email = email;
    this.phone = phone;
    this.company = company;
    this.status = status;
  }
}