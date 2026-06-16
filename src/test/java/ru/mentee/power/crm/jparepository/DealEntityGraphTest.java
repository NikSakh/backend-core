package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import ru.mentee.power.crm.domain.jpa.DealJpaEntity;
import ru.mentee.power.crm.domain.jpa.DealProduct;
import ru.mentee.power.crm.domain.jpa.Product;
import ru.mentee.power.crm.spring.Application;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = Application.class)
class DealEntityGraphTest {

  @Autowired private DealJpaRepository dealRepository;

  @Autowired private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    dealRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Test
  void shouldSaveDealWithProductsAndLoadWithEntityGraph() {
    Product product1 =
        productRepository.save(
            new Product(
                "Ноутбук",
                "LAPTOP-" + UUID.randomUUID().toString().substring(0, 6),
                new BigDecimal("90000"),
                true));
    Product product2 =
        productRepository.save(
            new Product(
                "Монитор",
                "MONITOR-" + UUID.randomUUID().toString().substring(0, 6),
                new BigDecimal("25000"),
                true));

    DealJpaEntity deal = new DealJpaEntity("Сделка", new BigDecimal("150000"), "NEW");
    deal.addDealProduct(new DealProduct(deal, product1, 2, new BigDecimal("81000")));
    deal.addDealProduct(new DealProduct(deal, product2, 1, new BigDecimal("25000")));
    DealJpaEntity saved = dealRepository.save(deal);

    Optional<DealJpaEntity> found = dealRepository.findDealWithProducts(saved.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getDealProducts()).hasSize(2);
    assertThat(found.get().getDealProducts().get(0).getQuantity()).isIn(1, 2);
    assertThat(found.get().getDealProducts().get(0).getUnitPrice()).isNotNull();
  }

  @Test
  void shouldLoadDealProductsWithMetadata() {
    Product product =
        productRepository.save(
            new Product(
                "Клавиатура",
                "KEY-" + UUID.randomUUID().toString().substring(0, 6),
                new BigDecimal("5000"),
                true));

    DealJpaEntity deal = new DealJpaEntity("Сделка с клавиатурой", new BigDecimal("10000"), "NEW");
    deal.addDealProduct(new DealProduct(deal, product, 3, new BigDecimal("4500")));
    dealRepository.save(deal);

    Optional<DealJpaEntity> found = dealRepository.findDealWithProducts(deal.getId());
    assertThat(found.get().getDealProducts().get(0).getQuantity()).isEqualTo(3);
    assertThat(found.get().getDealProducts().get(0).getUnitPrice())
        .isEqualTo(new BigDecimal("4500"));
  }
}
