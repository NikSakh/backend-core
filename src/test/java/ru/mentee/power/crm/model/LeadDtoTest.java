package ru.mentee.power.crm.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadDtoTest {

  @Test
  void shouldCreateLeadDto() {
    LeadDto dto = new LeadDto("1", "test@example.com", "+123", "Corp", LeadStatus.NEW);

    assertThat(dto.id()).isEqualTo("1");
    assertThat(dto.email()).isEqualTo("test@example.com");
    assertThat(dto.phone()).isEqualTo("+123");
    assertThat(dto.company()).isEqualTo("Corp");
    assertThat(dto.status()).isEqualTo(LeadStatus.NEW);
  }
}