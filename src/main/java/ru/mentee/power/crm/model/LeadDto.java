package ru.mentee.power.crm.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeadDto(
    String id,

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    String email,

    @Size(max = 20, message = "Телефон не должен превышать 20 символов")
    String phone,

    @NotBlank(message = "Название компании обязательно")
    @Size(max = 200, message = "Название компании не должно превышать 200 символов")
    String company,

    @NotNull(message = "Статус обязателен")
    LeadStatus status,

    String rejectionReasonId,
    String rejectionReasonName
) {
  public LeadDto(String id, String email, String phone, String company, LeadStatus status) {
    this(id, email, phone, company, status, null, null);
  }

  public LeadDto(String id, String email, String phone, String company, LeadStatus status,
                   String rejectionReasonId) {
    this(id, email, phone, company, status, rejectionReasonId, null);
  }
}