package ru.mentee.power.crm.spring.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.service.DealService;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
class DealControllerUnitTest {

  @Mock private DealService dealService;

  @Mock private LeadService leadService;

  @InjectMocks private DealController controller;

  @BeforeEach
  void setUp() {}

  @Test
  void shouldListDeals() {
    when(dealService.getAllDeals()).thenReturn(List.of());

    Model model = new ExtendedModelMap();
    String viewName = controller.listDeals(model);

    assertThat(viewName).isEqualTo("deals/list");
    assertThat(model.getAttribute("deals")).asList().isEmpty();
  }

  @Test
  void shouldShowKanbanView() {
    when(dealService.getDealsByStatusForKanban()).thenReturn(Map.of());

    Model model = new ExtendedModelMap();
    String viewName = controller.kanbanView(model);

    assertThat(viewName).isEqualTo("deals/kanban");
    assertThat(model.getAttribute("dealsByStatus")).isNotNull();
    assertThat(model.getAttribute("allStatuses")).isNotNull();
  }

  @Test
  void shouldConvertLeadToDeal() {
    String viewName = controller.convertLeadToDeal(UUID.randomUUID(), new BigDecimal("1000"));

    assertThat(viewName).isEqualTo("redirect:/deals");
  }

  @Test
  void shouldTransitionStatus() {
    String viewName = controller.transitionStatus(UUID.randomUUID(), DealStatus.QUALIFIED);

    assertThat(viewName).isEqualTo("redirect:/deals/kanban");
  }
}
