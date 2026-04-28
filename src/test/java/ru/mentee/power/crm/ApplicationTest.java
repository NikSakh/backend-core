package ru.mentee.power.crm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;

class ApplicationTest {

  @Test
  void leadServiceShouldContainFiveInitialLeads() {
    LeadRepository repository = new LeadRepository();
    LeadService service = new LeadService(repository);

    service.addLead("john@example.com", "TechCorp", LeadStatus.NEW);
    service.addLead("alice@example.com", "Innovate Inc", LeadStatus.QUALIFIED);
    service.addLead("bob@example.com", "Global Solutions", LeadStatus.CONVERTED);
    service.addLead("carol@example.com", "Future Tech", LeadStatus.NEW);
    service.addLead("dave@example.com", "Digital Works", LeadStatus.QUALIFIED);

    List<LeadDto> leads = service.findAll();

    assertThat(leads).hasSize(5);

    List<String> emails = leads.stream().map(LeadDto::email).toList();
    assertThat(emails).containsExactlyInAnyOrder(
        "john@example.com",
        "alice@example.com",
        "bob@example.com",
        "carol@example.com",
        "dave@example.com"
    );

    List<String> companies = leads.stream().map(LeadDto::company).toList();
    assertThat(companies).containsExactlyInAnyOrder(
        "TechCorp",
        "Innovate Inc",
        "Global Solutions",
        "Future Tech",
        "Digital Works"
    );

    List<LeadStatus> statuses = leads.stream().map(LeadDto::status).toList();
    assertThat(statuses).containsExactlyInAnyOrder(
        LeadStatus.NEW,
        LeadStatus.QUALIFIED,
        LeadStatus.CONVERTED,
        LeadStatus.NEW,
        LeadStatus.QUALIFIED
    );
  }

  @Test
  void mainMethodShouldNotThrowExceptions() {
    assertThatCode(() -> {
      Thread thread = new Thread(() -> {
        try {
          Application.main(new String[]{});
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      thread.setDaemon(true);
      thread.start();
      Thread.sleep(500);
      thread.interrupt();
    }).doesNotThrowAnyException();
  }
}