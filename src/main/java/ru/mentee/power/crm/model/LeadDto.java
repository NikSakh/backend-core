package ru.mentee.power.crm.model;

public record LeadDto(
    String id,
    String email,
    String phone,
    String company,
    LeadStatus status
) {
}