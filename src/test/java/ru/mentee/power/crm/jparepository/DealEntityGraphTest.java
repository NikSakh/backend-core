package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.domain.jpa.DealJpaEntity;
import ru.mentee.power.crm.domain.jpa.DealProduct;
import ru.mentee.power.crm.domain.jpa.Product;
import ru.mentee.power.crm.spring.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class DealEntityGraphTest {

  @Autowired
  private DealJpaRepository dealRepository;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void showEntityGraphJoin() {
    String uniqueSku1 = "LAPTOP-" + UUID.randomUUID().toString().substring(0, 6);
    String uniqueSku2 = "MONITOR-" + UUID.randomUUID().toString().substring(0, 6);

    Product product1 = productRepository.save(new Product("Ноутбук", uniqueSku1, new BigDecimal("90000"), true));
    Product product2 = productRepository.save(new Product("Монитор", uniqueSku2, new BigDecimal("25000"), true));

    DealJpaEntity deal = new DealJpaEntity("Сделка с ноутбуком и монитором", new BigDecimal("150000"), "NEW");
    deal.addDealProduct(new DealProduct(deal, product1, 2, new BigDecimal("81000")));
    deal.addDealProduct(new DealProduct(deal, product2, 1, new BigDecimal("25000")));
    DealJpaEntity saved = dealRepository.save(deal);

    System.out.println("=== @EntityGraph: ОДИН ЗАПРОС С JOIN ===");

    Optional<DealJpaEntity> found = dealRepository.findDealWithProducts(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getDealProducts()).hasSize(2);

    System.out.println("=== БЕЗ @EntityGraph было бы: ===");
    System.out.println("1) SELECT * FROM deals WHERE id = ?");
    System.out.println("2) SELECT * FROM deal_product WHERE deal_id = ?");
    System.out.println("3) SELECT * FROM products WHERE id = ? (для каждого продукта)");
    System.out.println("Итого: 1 + 1 + 2 = 4 запроса вместо одного с JOIN");
  }
}
