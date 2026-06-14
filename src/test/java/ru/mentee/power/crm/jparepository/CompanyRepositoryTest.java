package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.mentee.power.crm.domain.jpa.Company;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.spring.Application;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = Application.class)
class CompanyRepositoryTest {

  @Autowired private CompanyRepository companyRepository;

  @Autowired private LeadJpaRepository leadRepository;

  @BeforeEach
  void setUp() {
    leadRepository.deleteAll();
    companyRepository.deleteAll();
  }

  @Test
  void shouldCascadeSaveCompanyWithLeads() {
    Company company = new Company("Яндекс", "IT");
    company.addLead(new LeadJpaEntity("ivan@yandex.ru", "Яндекс", "NEW"));
    company.addLead(new LeadJpaEntity("maria@yandex.ru", "Яндекс", "CONTACTED"));

    Company saved = companyRepository.save(company);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getLeads()).hasSize(2);
  }

  @Test
  void shouldFindByIdWithLeadsUsingEntityGraph() {
    Company company = new Company("Сбер", "Finance");
    company.addLead(new LeadJpaEntity("petr@sber.ru", "Сбер", "NEW"));
    company.addLead(new LeadJpaEntity("olga@sber.ru", "Сбер", "QUALIFIED"));
    Company saved = companyRepository.save(company);

    Company found = companyRepository.findByIdWithLeads(saved.getId()).get();

    assertThat(found.getLeads()).hasSize(2);
    assertThat(found.getLeads().get(0).getCompanyRef()).isEqualTo(found);
  }

  @Test
  void shouldFindByName() {
    companyRepository.save(new Company("Тинькофф", "Finance"));

    Company found = companyRepository.findByName("Тинькофф").get();

    assertThat(found.getIndustry()).isEqualTo("Finance");
  }

  @Test
  void shouldReturnEmptyWhenCompanyNotFound() {
    assertThat(companyRepository.findByName("Несуществующая")).isEmpty();
  }

  @Test
  void shouldSyncBothSidesOfRelation() {
    Company company = new Company("VK", "Tech");
    LeadJpaEntity lead = new LeadJpaEntity("lead@vk.ru", "VK", "NEW");
    company.addLead(lead);
    companyRepository.save(company);

    LeadJpaEntity foundLead = leadRepository.findById(lead.getId()).get();

    assertThat(foundLead.getCompanyRef()).isNotNull();
    assertThat(foundLead.getCompanyRef().getName()).isEqualTo("VK");
  }
}
