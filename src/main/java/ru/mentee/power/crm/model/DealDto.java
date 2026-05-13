package ru.mentee.power.crm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ru.mentee.power.crm.domain.DealStatus;

public record DealDto(
    String id,
    String leadId,
    BigDecimal amount,
    DealStatus status,
    LocalDateTime createdAt
) {}
