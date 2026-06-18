package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateLeadRequest {

  @NotBlank(message = "Email обязателен")
  @Email(message = "Некорректный формат email")
  @Size(max = 100, message = "Email не должен превышать 100 символов")
  private String email;

  @Size(max = 20, message = "Телефон не должен превышать 20 символов")
  private String phone;

  @NotBlank(message = "Название компании обязательно")
  @Size(max = 200, message = "Название компании не должно превышать 200 символов")
  private String company;

  @NotBlank(message = "Статус обязателен")
  private String status;

  public CreateLeadRequest() {}

  public CreateLeadRequest(String email, String phone, String company, String status) {
    this.email = email;
    this.phone = phone;
    this.company = company;
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getCompany() {
    return company;
  }

  public String getStatus() {
    return status;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
