package ru.mentee.power.crm.jparepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.mentee.power.crm.domain.jpa.Product;
import ru.mentee.power.crm.jpa.JpaConfig;
import ru.mentee.power.crm.spring.Application;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {Application.class, JpaConfig.class})
class ProductRepositoryTest {

  @Autowired private ProductRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void shouldSaveAndFindProduct() {
    Product product = new Product("Ноутбук", "LAPTOP-001", new BigDecimal("50000.00"), true);
    Product saved = repository.save(product);

    Optional<Product> found = repository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getSku()).isEqualTo("LAPTOP-001");
    assertThat(found.get().getPrice()).isEqualTo(new BigDecimal("50000.00"));
  }

  @Test
  void shouldFindBySku() {
    repository.save(new Product("Монитор", "MONITOR-001", new BigDecimal("25000.00"), true));

    Optional<Product> found = repository.findBySku("MONITOR-001");
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Монитор");
  }

  @Test
  void shouldFindByActiveTrue() {
    repository.save(new Product("Товар 1", "SKU-1", new BigDecimal("100"), true));
    repository.save(new Product("Товар 2", "SKU-2", new BigDecimal("200"), true));
    repository.save(new Product("Товар 3", "SKU-3", new BigDecimal("300"), false));

    List<Product> activeProducts = repository.findByActiveTrue();
    assertThat(activeProducts).hasSize(2);
  }

  @Test
  void shouldEnforceUniqueSkuConstraint() {
    repository.save(new Product("Первый", "UNIQUE-SKU", new BigDecimal("100"), true));

    assertThatThrownBy(
            () -> {
              repository.save(new Product("Второй", "UNIQUE-SKU", new BigDecimal("200"), true));
              repository.flush();
            })
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
