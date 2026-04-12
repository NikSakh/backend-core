package ru.mentee.power.crm.model;

public record Lead(
    String id,
    String email,
    String phone,
    String company,
    LeadStatus status
) {
}