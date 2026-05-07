package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FieldInjectionProblemTest {

  @Test
  void fieldInjectionCausesNullPointerWithoutSpring() {
    DemoController controller = new DemoController(null);
    assertThat(controller).isNotNull();
  }
}
