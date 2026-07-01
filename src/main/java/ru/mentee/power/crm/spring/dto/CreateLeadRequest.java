package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateLeadRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Must be a valid email")
  private String email;

  @NotBlank(message = "Company is required")
  @Size(min = 2, max = 100, message = "Company must be between 2 and 100 characters")
  private String company;

  private String phone;
  private String status;

  public CreateLeadRequest() {}

  public CreateLeadRequest(String email, String phone, String company, String status) {
    this.email = email;
    this.phone = phone;
    this.company = company;
    this.status = status;
  }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getCompany() { return company; }
  public void setCompany(String company) { this.company = company; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
}