package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class DealTest {

  @Test
  void shouldCreateDealWithNewStatus() {
    UUID leadId = UUID.randomUUID();
    BigDecimal amount = new BigDecimal("100000.00");

    Deal deal = new Deal(leadId, amount);

    assertThat(deal.getId()).isNotNull();
    assertThat(deal.getLeadId()).isEqualTo(leadId);
    assertThat(deal.getAmount()).isEqualTo(amount);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.NEW);
    assertThat(deal.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTransitionToValidStatus() {
    Deal deal = new Deal(UUID.randomUUID(), new BigDecimal("50000"));
    deal.transitionTo(DealStatus.QUALIFIED);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.QUALIFIED);
  }

  @Test
  void shouldThrowExceptionWhenTransitionInvalid() {
    Deal deal = new Deal(
        UUID.randomUUID(),
        UUID.randomUUID(),
        new BigDecimal("50000"),
        DealStatus.WON,
        LocalDateTime.now()
    );

    assertThatThrownBy(() -> deal.transitionTo(DealStatus.NEW))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot transition from WON to NEW");
  }

  @Test
  void shouldNotAllowDirectStatusChangeFromNewToWon() {
    Deal deal = new Deal(UUID.randomUUID(), new BigDecimal("50000"));
    assertThatThrownBy(() -> deal.transitionTo(DealStatus.WON))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cannot transition from NEW to WON");
  }

  @Test
  void shouldAllowValidTransitionChain() {
    Deal deal = new Deal(UUID.randomUUID(), new BigDecimal("50000"));
    deal.transitionTo(DealStatus.QUALIFIED);
    deal.transitionTo(DealStatus.PROPOSAL_SENT);
    deal.transitionTo(DealStatus.NEGOTIATION);
    deal.transitionTo(DealStatus.WON);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.WON);
  }

  @Test
  void shouldAllowTransitionToLostFromAnyNonTerminalState() {
    Deal deal = new Deal(UUID.randomUUID(), new BigDecimal("50000"));
    deal.transitionTo(DealStatus.LOST);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.LOST);
  }

  @Test
  void shouldCreateDealWithFullConstructor() {
    UUID id = UUID.randomUUID();
    UUID leadId = UUID.randomUUID();
    BigDecimal amount = new BigDecimal("200000.00");
    LocalDateTime createdAt = LocalDateTime.now();

    Deal deal = new Deal(id, leadId, amount, DealStatus.QUALIFIED, createdAt);

    assertThat(deal.getId()).isEqualTo(id);
    assertThat(deal.getLeadId()).isEqualTo(leadId);
    assertThat(deal.getAmount()).isEqualTo(amount);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.QUALIFIED);
    assertThat(deal.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldBeEqualWhenSameId() {
    UUID id = UUID.randomUUID();
    Deal deal1 = new Deal(id, UUID.randomUUID(),
        new BigDecimal("1000"), DealStatus.NEW, LocalDateTime.now());
    Deal deal2 = new Deal(id, UUID.randomUUID(),
        new BigDecimal("2000"), DealStatus.WON, LocalDateTime.now());

    assertThat(deal1).isEqualTo(deal2);
    assertThat(deal1.hashCode()).isEqualTo(deal2.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentId() {
    Deal deal1 = new Deal(UUID.randomUUID(), new BigDecimal("1000"));
    Deal deal2 = new Deal(UUID.randomUUID(), new BigDecimal("1000"));

    assertThat(deal1).isNotEqualTo(deal2);
  }
}