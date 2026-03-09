package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LeadTest {
  @Test
  void shouldReturnIdWhenGetIdCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String id = lead.getId();
    assertThat(id).isEqualTo("L1");
  }

  @Test
  void shouldReturnEmailWhenGetEmailCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String email = lead.getEmail();
    assertThat(email).isEqualTo("test@example.com");
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String phone = lead.getPhone();
    assertThat(phone).isEqualTo("+71234567890");
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String company = lead.getCompany();
    assertThat(company).isEqualTo("TestCorp");
  }

  @Test
  void shouldReturnStatusWhenGetStatusCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String status = lead.getStatus();
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  void shouldReturnFormattedStringWhenToStringCalled() {
    Lead lead = new Lead("L1", "test@example.com", "+71234567890", "TestCorp", "NEW");
    String result = lead.toString();
    assertThat(result).isEqualTo(
        "Lead{id='L1', email='test@example.com', "
            + "phone='+71234567890', company='TestCorp', status='NEW'}"
    );
  }
}