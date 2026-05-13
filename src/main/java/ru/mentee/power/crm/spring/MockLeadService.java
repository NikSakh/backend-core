package ru.mentee.power.crm.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;

public class MockLeadService extends LeadService {

  private final List<LeadDto> mockLeads;

  public MockLeadService() {
    super(new LeadRepository());
    this.mockLeads = new ArrayList<>(List.of(
        new LeadDto("11111111-1111-1111-1111-111111111111",
            "test1@example.com", "+1234567890", "TestCorp1", LeadStatus.NEW),
        new LeadDto("22222222-2222-2222-2222-222222222222",
            "test2@example.com", "+0987654321", "TestCorp2", LeadStatus.QUALIFIED)
    ));
  }

  @Override
  public List<LeadDto> findAll() {
    return new ArrayList<>(mockLeads);
  }

  @Override
  public List<LeadDto> findByStatus(LeadStatus status) {
    return mockLeads.stream()
        .filter(lead -> lead.status() == status)
        .toList();
  }

  @Override
  public Optional<LeadDto> findById(UUID id) {
    return mockLeads.stream()
        .filter(lead -> lead.id().equals(id.toString()))
        .findFirst();
  }

  @Override
  public LeadDto update(UUID id, String email, String phone, String company, LeadStatus status) {
    for (int i = 0; i < mockLeads.size(); i++) {
      if (mockLeads.get(i).id().equals(id.toString())) {
        LeadDto updated = new LeadDto(id.toString(), email, phone, company, status);
        mockLeads.set(i, updated);
        return updated;
      }
    }
    throw new RuntimeException("Lead not found");
  }

  @Override
  public void delete(UUID id) {
    mockLeads.removeIf(lead -> lead.id().equals(id.toString()));
  }

  @Override
  public List<LeadDto> findLeads(String search, String status) {
    return mockLeads.stream()
        .filter(lead -> {
          boolean matchesSearch = search == null || search.isEmpty()
              || lead.email().toLowerCase().contains(search.toLowerCase())
              || lead.company().toLowerCase().contains(search.toLowerCase());
          boolean matchesStatus = status == null || status.isEmpty()
              || lead.status().name().equalsIgnoreCase(status);
          return matchesSearch && matchesStatus;
        })
        .toList();
  }
}