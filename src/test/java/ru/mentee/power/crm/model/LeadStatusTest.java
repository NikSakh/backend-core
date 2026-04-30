package ru.mentee.power.crm.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadStatusTest {

  @Test
  void shouldHaveThreeStatuses() {
    assertThat(LeadStatus.values()).hasSize(3);
  }

  @Test
  void shouldContainNew() {
    assertThat(LeadStatus.valueOf("NEW")).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldContainQualified() {
    assertThat(LeadStatus.valueOf("QUALIFIED")).isEqualTo(LeadStatus.QUALIFIED);
  }

  @Test
  void shouldContainConverted() {
    assertThat(LeadStatus.valueOf("CONVERTED")).isEqualTo(LeadStatus.CONVERTED);
  }
}