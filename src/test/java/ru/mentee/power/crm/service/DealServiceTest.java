package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.domain.LeadEntity;
import ru.mentee.power.crm.model.DealDto;
import ru.mentee.power.crm.repository.DealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

  @Mock private DealRepository dealRepository;

  @Mock private LeadRepository leadRepository;

  @InjectMocks private DealService dealService;

  private UUID leadId;
  private UUID dealId;
  private Deal testDeal;

  @BeforeEach
  void setUp() {
    leadId = UUID.randomUUID();
    dealId = UUID.randomUUID();
    testDeal =
        new Deal(
            dealId,
            leadId,
            new BigDecimal("100000.00"),
            DealStatus.NEW,
            java.time.LocalDateTime.now());
  }

  @Test
  void shouldConvertLeadToDeal() {
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123", address);
    LeadEntity lead = new LeadEntity(leadId, contact, "TestCorp", "QUALIFIED");

    when(leadRepository.findById(leadId.toString())).thenReturn(Optional.of(lead));

    DealDto result = dealService.convertLeadToDeal(leadId, new BigDecimal("150000.00"));

    assertThat(result.leadId()).isEqualTo(leadId.toString());
    assertThat(result.amount()).isEqualTo(new BigDecimal("150000.00"));
    assertThat(result.status()).isEqualTo(DealStatus.NEW);
    verify(dealRepository).save(any(Deal.class));
  }

  @Test
  void shouldThrowExceptionWhenLeadNotFound() {
    when(leadRepository.findById(leadId.toString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> dealService.convertLeadToDeal(leadId, new BigDecimal("1000")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Lead not found");
  }

  @Test
  void shouldTransitionDealStatus() {
    when(dealRepository.findById(dealId)).thenReturn(Optional.of(testDeal));

    DealDto result = dealService.transitionDealStatus(dealId, DealStatus.QUALIFIED);

    assertThat(result.status()).isEqualTo(DealStatus.QUALIFIED);
    verify(dealRepository).save(any(Deal.class));
  }

  @Test
  void shouldThrowExceptionWhenDealNotFound() {
    when(dealRepository.findById(dealId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> dealService.transitionDealStatus(dealId, DealStatus.QUALIFIED))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Deal not found");
  }

  @Test
  void shouldGetAllDeals() {
    when(dealRepository.findAll()).thenReturn(List.of(testDeal));

    List<DealDto> result = dealService.getAllDeals();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).id()).isEqualTo(dealId.toString());
  }

  @Test
  void shouldGetDealsByStatusForKanban() {
    when(dealRepository.findAll()).thenReturn(List.of(testDeal));

    Map<DealStatus, List<DealDto>> result = dealService.getDealsByStatusForKanban();

    assertThat(result).containsKey(DealStatus.NEW);
    assertThat(result.get(DealStatus.NEW)).hasSize(1);
  }
}
