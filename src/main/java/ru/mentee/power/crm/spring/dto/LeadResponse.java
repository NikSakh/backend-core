package ru.mentee.power.crm.spring.dto;

public record LeadResponse(
    String id,
    String email,
    String phone,
    String company,
    String status,
    String rejectionReasonName) {}
