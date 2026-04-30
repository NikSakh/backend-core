package ru.mentee.power.crm.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadStatusTest {

  @Test
  void shouldHaveFiveStatuses() {
    assertThat(LeadStatus.values()).hasSize(5);
  }

  @Test
  void shouldHaveCorrectDisplayNames() {
    assertThat(LeadStatus.NEW.getDisplayName()).isEqualTo("Новый");
    assertThat(LeadStatus.CONTACTED.getDisplayName()).isEqualTo("В контакте");
    assertThat(LeadStatus.QUALIFIED.getDisplayName()).isEqualTo("Квалифицирован");
    assertThat(LeadStatus.CONVERTED.getDisplayName()).isEqualTo("Конвертирован");
    assertThat(LeadStatus.LOST.getDisplayName()).isEqualTo("Потерян");
  }

  @Test
  void shouldContainAllStatuses() {
    assertThat(LeadStatus.values())
        .containsExactlyInAnyOrder(
            LeadStatus.NEW,
            LeadStatus.CONTACTED,
            LeadStatus.QUALIFIED,
            LeadStatus.CONVERTED,
            LeadStatus.LOST
        );
  }
}