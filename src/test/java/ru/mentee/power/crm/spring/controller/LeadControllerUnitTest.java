package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.MockLeadService;

class LeadControllerUnitTest {

  @Test
  void shouldCreateControllerWithoutSpring() {
    MockLeadService mockService = new MockLeadService();
    LeadController controller = new LeadController(mockService);

    Model model = new ExtendedModelMap();
    String viewName = controller.showLeads(null, model);

    assertThat(viewName).isEqualTo("leads/list");
    assertThat(model.getAttribute("leads")).asList().hasSize(2);
  }

  @Test
  void shouldFilterByStatus() {
    MockLeadService mockService = new MockLeadService();
    LeadController controller = new LeadController(mockService);

    Model model = new ExtendedModelMap();
    controller.showLeads(LeadStatus.NEW, model);

    @SuppressWarnings("unchecked")
    var leads = (java.util.List<?>) model.getAttribute("leads");
    assertThat(leads).hasSize(1);
    assertThat(model.getAttribute("currentFilter")).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldNotRequireSpringContext() {
    LeadController controller = new LeadController(new MockLeadService());
    assertThat(controller).isNotNull();
  }
}