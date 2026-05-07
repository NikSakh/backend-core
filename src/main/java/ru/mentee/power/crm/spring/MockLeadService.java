package ru.mentee.power.crm.spring;

import java.util.List;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;

public class MockLeadService extends LeadService {

  private final List<LeadDto> mockLeads;

  public MockLeadService() {
    super(new LeadRepository()); // пустой репозиторий, не используется
    this.mockLeads = List.of(
        new LeadDto("1", "test1@example.com", "+1234567890", "TestCorp1", LeadStatus.NEW),
        new LeadDto("2", "test2@example.com", "+0987654321", "TestCorp2", LeadStatus.QUALIFIED)
    );
  }

  @Override
  public List<LeadDto> findAll() {
    return mockLeads;
  }

  @Override
  public List<LeadDto> findByStatus(LeadStatus status) {
    return mockLeads.stream()
        .filter(lead -> lead.status() == status)
        .toList();
  }
}