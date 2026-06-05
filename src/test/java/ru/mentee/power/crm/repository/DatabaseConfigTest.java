package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class DatabaseConfigTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldConnectToH2Database() {
    String productName = jdbcTemplate.execute((ConnectionCallback<String>) connection ->
        connection.getMetaData().getDatabaseProductName()
    );
    assertThat(productName).isEqualTo("H2");
  }

  @Test
  void shouldHaveLeadsTableCreated() {
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'LEADS'",
        Integer.class
    );
    assertThat(count).isEqualTo(1);
  }
}