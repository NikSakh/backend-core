package ru.mentee.power.crm.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateLeadRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Must be a valid email")
  private String email;

  @NotBlank(message = "Company is required")
  @Size(min = 2, max = 100, message = "Company must be between 2 and 100 characters")
  private String company;

  private String phone;
  private String status;
}
