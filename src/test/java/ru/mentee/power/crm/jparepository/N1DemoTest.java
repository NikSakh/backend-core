package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.Company;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.spring.Application;

@Disabled("Requires PostgreSQL — run locally")
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class N1DemoTest {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  void showN1ProblemSolved() {
    Company company = new Company("Яндекс", "IT");
    company.addLead(new LeadJpaEntity("ivan@yandex.ru", "Яндекс", "NEW"));
    company.addLead(new LeadJpaEntity("maria@yandex.ru", "Яндекс", "CONTACTED"));
    Company saved = companyRepository.save(company);

    Optional<Company> found = companyRepository.findByIdWithLeads(saved.getId());
    assertThat(found.get().getLeads()).hasSize(2);
  }
}