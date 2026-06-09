package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.Company;
import ru.mentee.power.crm.domain.jpa.LeadJpaEntity;
import ru.mentee.power.crm.spring.Application;

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

    System.out.println("");
    System.out.println("=== РЕШЕНИЕ N+1: ОДИН ЗАПРОС С LEFT JOIN ===");
    System.out.println("SQL: SELECT c.*, "
        + "l.* FROM companies c LEFT JOIN leads l ON c.id = l.company_id WHERE c.id = ?");
    System.out.println("");

    Optional<Company> found = companyRepository.findByIdWithLeads(saved.getId());
    assertThat(found.get().getLeads()).hasSize(2);

    System.out.println("=== БЕЗ @EntityGraph БЫЛО БЫ N+1 ===");
    System.out.println("1) SELECT * FROM companies WHERE id = ?");
    System.out.println("2) SELECT * FROM leads WHERE company_id = ? (для каждого лида)");
    System.out.println("Итого: 1 + N запросов вместо одного с JOIN");
  }
}