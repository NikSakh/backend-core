package ru.mentee.power.crm.spring.rest.fixed.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateStatusRequest(@NotBlank String status) {}
