package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

@ExtendWith(MockitoExtension.class)
class LeadServiceTest {

  @Mock
  private LeadRepository mockRepository;

  @InjectMocks
  private LeadService service;

  @BeforeEach
  void setUp() {
  }

  @Test
  void shouldCallRepositorySaveWhenAddingNewLead() {
    when(mockRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());

    when(mockRepository.save(any(Lead.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Lead result = service.addLead("new@example.com", "Company", LeadStatus.NEW);

    verify(mockRepository, times(1)).save(any(Lead.class));

    assertThat(result.email()).isEqualTo("new@example.com");
    assertThat(result.company()).isEqualTo("Company");
    assertThat(result.status()).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldNotCallSaveWhenEmailExists() {
    UUID existingId = UUID.randomUUID();
    Lead existingLead = new Lead(
        existingId.toString(),
        "existing@example.com",
        null,
        "Existing Company",
        LeadStatus.CONTACTED
    );
    when(mockRepository.findByEmail("existing@example.com"))
        .thenReturn(Optional.of(existingLead));

    assertThatThrownBy(() ->
        service.addLead("existing@example.com", "New Company", LeadStatus.NEW)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email 'existing@example.com' already exists");

    verify(mockRepository, never()).save(any(Lead.class));
  }

  @Test
  void shouldCallFindByEmailBeforeSave() {
    when(mockRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());
    when(mockRepository.save(any(Lead.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    service.addLead("test@example.com", "Company", LeadStatus.NEW);

    var inOrder = inOrder(mockRepository);
    inOrder.verify(mockRepository).findByEmail("test@example.com");
    inOrder.verify(mockRepository).save(any(Lead.class));
  }

  @Test
  void shouldNormalizeEmailWhenAddingLead() {
    when(mockRepository.findByEmail("test@example.com"))
        .thenReturn(Optional.empty());
    when(mockRepository.save(any(Lead.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Lead result = service.addLead("  TEST@EXAMPLE.COM  ", "Company", LeadStatus.NEW);

    assertThat(result.email()).isEqualTo("test@example.com");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenEmailIsNull() {
    assertThatThrownBy(() -> service.addLead(null, "Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be null or empty");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenEmailIsEmpty() {
    assertThatThrownBy(() -> service.addLead("   ", "Company", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be null or empty");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenCompanyIsNull() {
    assertThatThrownBy(() -> service.addLead("test@example.com", null, LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Company cannot be null or empty");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenCompanyIsEmpty() {
    assertThatThrownBy(() -> service.addLead("test@example.com", "   ", LeadStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Company cannot be null or empty");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenStatusIsNull() {
    assertThatThrownBy(() -> service.addLead("test@example.com", "Company", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Status cannot be null");
  }

  @Test
  void findAllShouldDelegateToRepository() {
    List<Lead> expectedLeads = List.of(
        new Lead("1", "lead1@example.com", null, "Company 1", LeadStatus.NEW),
        new Lead("2", "lead2@example.com", null, "Company 2", LeadStatus.CONTACTED)
    );
    when(mockRepository.findAll()).thenReturn(expectedLeads);

    List<Lead> result = service.findAll();

    assertThat(result).isEqualTo(expectedLeads);
    verify(mockRepository, times(1)).findAll();
  }

  @Test
  void findByIdShouldDelegateToRepository() {
    UUID id = UUID.randomUUID();
    Lead expectedLead = new Lead(id.toString(), "test@example.com", null,
        "Company", LeadStatus.NEW);
    when(mockRepository.findById(id.toString())).thenReturn(Optional.of(expectedLead));

    Optional<Lead> result = service.findById(id);

    assertThat(result).hasValue(expectedLead);
    verify(mockRepository, times(1)).findById(id.toString());
  }

  @Test
  void findByEmailShouldDelegateToRepository() {
    String email = "search@example.com";
    Lead expectedLead = new Lead("1", email, null, "Search Company", LeadStatus.NEW);
    when(mockRepository.findByEmail(email)).thenReturn(Optional.of(expectedLead));

    Optional<Lead> result = service.findByEmail(email);

    assertThat(result).hasValue(expectedLead);
    verify(mockRepository, times(1)).findByEmail(email);
  }
}