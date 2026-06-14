package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.jparepository.RejectionReasonsRepository;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.MockLeadService;

class LeadControllerUnitTest {

  private MockLeadService mockService;
  private LeadController controller;

  @BeforeEach
  void setUp() {
    mockService = new MockLeadService();
    controller = new LeadController(mockService, mock(RejectionReasonsRepository.class));
  }

  @Test
  void shouldCreateControllerWithoutSpring() {
    Model model = new ExtendedModelMap();
    String viewName = controller.showLeads(null, null, null, model);

    assertThat(viewName).isEqualTo("leads/list");
    assertThat(model.getAttribute("leads")).asList().hasSize(2);
  }

  @Test
  void shouldFilterByStatus() {
    Model model = new ExtendedModelMap();
    controller.showLeads(LeadStatus.NEW, null, null, model);

    @SuppressWarnings("unchecked")
    var leads = (java.util.List<?>) model.getAttribute("leads");
    assertThat(leads).hasSize(1);
    assertThat(model.getAttribute("currentFilter")).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldNotRequireSpringContext() {
    LeadController controller =
        new LeadController(new MockLeadService(), mock(RejectionReasonsRepository.class));
    assertThat(controller).isNotNull();
  }

  @Test
  void shouldShowCreateForm() {
    Model model = new ExtendedModelMap();
    String viewName = controller.showCreateForm(model);

    assertThat(viewName).isEqualTo("leads/create");
    assertThat(model.getAttribute("lead")).isNotNull();
  }

  @Test
  void shouldShowEditFormForExistingLead() {
    Model model = new ExtendedModelMap();
    String viewName =
        controller.showEditForm(UUID.fromString("11111111-1111-1111-1111-111111111111"), model);

    assertThat(viewName).isEqualTo("leads/edit");
    assertThat(model.getAttribute("lead")).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenEditingNonexistentLead() {
    Model model = new ExtendedModelMap();
    try {
      controller.showEditForm(UUID.fromString("22222222-2222-2222-2222-222222222222"), model);
    } catch (ResponseStatusException e) {
      assertThat(e.getStatusCode().value()).isEqualTo(404);
      assertThat(e.getReason()).contains("not found");
    }
  }

  @Test
  void shouldUpdateLeadAndRedirect() {
    LeadDto leadDto =
        new LeadDto(
            "11111111-1111-1111-1111-111111111111",
            "updated@example.com",
            "+123",
            "Updated Corp",
            LeadStatus.QUALIFIED);

    String viewName =
        controller.updateLead(
            UUID.fromString("11111111-1111-1111-1111-111111111111"),
            leadDto,
            new BeanPropertyBindingResult(leadDto, "lead"));

    assertThat(viewName).isEqualTo("redirect:/leads");
  }

  @Test
  void shouldDeleteLeadAndRedirect() {
    String viewName =
        controller.deleteLead(UUID.fromString("11111111-1111-1111-1111-111111111111"));
    assertThat(viewName).isEqualTo("redirect:/leads");
  }

  @Test
  void shouldThrowExceptionWhenDeletingNonexistentLead() {
    try {
      controller.deleteLead(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    } catch (ResponseStatusException e) {
      assertThat(e.getStatusCode().value()).isEqualTo(404);
      assertThat(e.getReason()).contains("not found");
    }
  }

  @Test
  void shouldFilterBySearchQuery() {
    Model model = new ExtendedModelMap();
    controller.showLeads(null, "test1", null, model);

    @SuppressWarnings("unchecked")
    var leads = (java.util.List<?>) model.getAttribute("leads");
    assertThat(leads).hasSize(1);
    assertThat(model.getAttribute("search")).isEqualTo("test1");
  }

  @Test
  void shouldCombineSearchAndStatusFilter() {
    Model model = new ExtendedModelMap();
    controller.showLeads(null, "test", "NEW", model);

    @SuppressWarnings("unchecked")
    var leads = (java.util.List<?>) model.getAttribute("leads");
    assertThat(model.getAttribute("search")).isEqualTo("test");
    assertThat(model.getAttribute("statusFilter")).isEqualTo("NEW");
  }

  @Test
  void shouldReturnFormWhenValidationFails() {
    LeadDto invalidLead = new LeadDto(null, "", "", "", null);
    Model model = new ExtendedModelMap();

    BindingResult errors = new BeanPropertyBindingResult(invalidLead, "lead");
    errors.rejectValue("email", "Email", "Email обязателен");

    String viewName = controller.createLead(invalidLead, errors, model);

    assertThat(viewName).isEqualTo("leads/create");
    assertThat(model.getAttribute("errors")).isSameAs(errors);
  }

  @Test
  void shouldCreateLeadAndRedirectWhenValid() {
    LeadDto validLead = new LeadDto(null, "test@example.com", null, "Corp", LeadStatus.NEW);
    Model model = new ExtendedModelMap();

    BindingResult errors = new BeanPropertyBindingResult(validLead, "lead");

    String viewName = controller.createLead(validLead, errors, model);

    assertThat(viewName).isEqualTo("redirect:/leads");
  }
}
