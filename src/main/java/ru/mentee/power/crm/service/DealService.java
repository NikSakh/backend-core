package ru.mentee.power.crm.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.domain.LeadEntity;
import ru.mentee.power.crm.model.DealDto;
import ru.mentee.power.crm.repository.DealRepository;
import ru.mentee.power.crm.repository.LeadRepository;

@Service
public class DealService {
  private final DealRepository dealRepository;
  private final LeadRepository leadRepository;

  public DealService(DealRepository dealRepository, LeadRepository leadRepository) {
    this.dealRepository = dealRepository;
    this.leadRepository = leadRepository;
  }

  public DealDto convertLeadToDeal(UUID leadId, BigDecimal amount) {
    Optional<LeadEntity> leadOpt = leadRepository.findById(leadId.toString());
    if (leadOpt.isEmpty()) {
      throw new IllegalArgumentException("Lead not found with id: " + leadId);
    }
    Deal deal = new Deal(leadId, amount);
    dealRepository.save(deal);
    return convertToDto(deal);
  }

  public DealDto transitionDealStatus(UUID dealId, DealStatus newStatus) {
    Optional<Deal> dealOpt = dealRepository.findById(dealId);
    if (dealOpt.isEmpty()) {
      throw new IllegalArgumentException("Deal not found with id: " + dealId);
    }
    Deal deal = dealOpt.get();
    deal.transitionTo(newStatus);
    dealRepository.save(deal);
    return convertToDto(deal);
  }

  public List<DealDto> getAllDeals() {
    return dealRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public Map<DealStatus, List<DealDto>> getDealsByStatusForKanban() {
    return dealRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.groupingBy(DealDto::status));
  }

  private DealDto convertToDto(Deal deal) {
    return new DealDto(
        deal.getId().toString(),
        deal.getLeadId().toString(),
        deal.getAmount(),
        deal.getStatus(),
        deal.getCreatedAt());
  }
}
