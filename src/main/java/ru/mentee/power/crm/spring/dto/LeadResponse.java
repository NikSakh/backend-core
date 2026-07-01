package ru.mentee.power.crm.spring.dto;

import java.util.UUID;

public record LeadResponse(UUID id, String email, String phone, String company, String status) {}
