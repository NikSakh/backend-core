package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.LeadEntity;
import ru.mentee.power.crm.model.LeadDto;
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

    when(mockRepository.save(any(LeadEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    LeadDto result = service.addLead("new@example.com", "Company", LeadStatus.NEW);

    verify(mockRepository, times(1)).save(any(LeadEntity.class));

    assertThat(result.email()).isEqualTo("new@example.com");
    assertThat(result.company()).isEqualTo("Company");
    assertThat(result.status()).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldNotCallSaveWhenEmailExists() {
    UUID existingId = UUID.randomUUID();
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("existing@example.com", "+123456789", address);
    LeadEntity existingLead = new LeadEntity(existingId, contact, "Existing Company", "QUALIFIED");

    when(mockRepository.findByEmail("existing@example.com"))
        .thenReturn(Optional.of(existingLead));

    assertThatThrownBy(() ->
        service.addLead("existing@example.com", "New Company", LeadStatus.NEW)
    )
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email 'existing@example.com' already exists");

    verify(mockRepository, never()).save(any(LeadEntity.class));
  }

  @Test
  void shouldCallFindByEmailBeforeSave() {
    when(mockRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());
    when(mockRepository.save(any(LeadEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    service.addLead("test@example.com", "Company", LeadStatus.NEW);

    var inOrder = inOrder(mockRepository);
    inOrder.verify(mockRepository).findByEmail("test@example.com");
    inOrder.verify(mockRepository).save(any(LeadEntity.class));
  }

  @Test
  void shouldNormalizeEmailWhenAddingLead() {
    when(mockRepository.findByEmail("test@example.com"))
        .thenReturn(Optional.empty());
    when(mockRepository.save(any(LeadEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    LeadDto result = service.addLead("  TEST@EXAMPLE.COM  ", "Company", LeadStatus.NEW);

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
    Address addressFirst = new Address("CityFirst", "StreetFirst", "11111");
    Contact contactFirst = new Contact("leadFirst@example.com", "+111111111", addressFirst);
    LeadEntity entityFirst = new LeadEntity(UUID.randomUUID(), contactFirst,
        "Company First", "NEW");

    Address addressSecond = new Address("CitySecond", "StreetSecond", "22222");
    Contact contactSecond = new Contact("leadSecond@example.com", "+222222222", addressSecond);
    LeadEntity entitySecond = new LeadEntity(UUID.randomUUID(), contactSecond,
        "Company Second", "QUALIFIED");

    List<LeadEntity> entities = List.of(entityFirst, entitySecond);
    when(mockRepository.findAll()).thenReturn(entities);

    List<LeadDto> result = service.findAll();

    assertThat(result).hasSize(2);

    assertThat(result).anySatisfy(lead -> {
      assertThat(lead.email()).isEqualTo("leadFirst@example.com");
      assertThat(lead.company()).isEqualTo("Company First");
      assertThat(lead.status()).isEqualTo(LeadStatus.NEW);
    });

    assertThat(result).anySatisfy(lead -> {
      assertThat(lead.email()).isEqualTo("leadSecond@example.com");
      assertThat(lead.company()).isEqualTo("Company Second");
      assertThat(lead.status()).isEqualTo(LeadStatus.QUALIFIED);
    });

    verify(mockRepository, times(1)).findAll();
  }

  @Test
  void findByIdShouldDelegateToRepository() {
    UUID id = UUID.randomUUID();
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("test@example.com", "+123456789", address);
    LeadEntity entity = new LeadEntity(id, contact, "Company", "NEW");

    when(mockRepository.findById(id.toString())).thenReturn(Optional.of(entity));

    Optional<LeadDto> result = service.findById(id);

    assertThat(result).isPresent();
    assertThat(result.get().id()).isEqualTo(id.toString());
    assertThat(result.get().email()).isEqualTo("test@example.com");
    assertThat(result.get().company()).isEqualTo("Company");
    assertThat(result.get().status()).isEqualTo(LeadStatus.NEW);
    verify(mockRepository, times(1)).findById(id.toString());
  }

  @Test
  void findByEmailShouldDelegateToRepository() {
    String email = "search@example.com";
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact(email, "+123456789", address);
    LeadEntity entity = new LeadEntity(UUID.randomUUID(), contact, "Search Company", "NEW");

    when(mockRepository.findByEmail(email)).thenReturn(Optional.of(entity));

    Optional<LeadDto> result = service.findByEmail(email);

    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo(email);
    assertThat(result.get().company()).isEqualTo("Search Company");
    assertThat(result.get().status()).isEqualTo(LeadStatus.NEW);
    verify(mockRepository, times(1)).findByEmail(email);
  }

  @Test
  void shouldNormalizeEmailWhenSearchingByEmail() {
    String email = "  SEARCH@EXAMPLE.COM  ";
    Address address = new Address("City", "Street", "12345");
    Contact contact = new Contact("search@example.com", "+123456789", address);
    LeadEntity entity = new LeadEntity(UUID.randomUUID(), contact, "Search Company", "NEW");

    when(mockRepository.findByEmail("search@example.com")).thenReturn(Optional.of(entity));

    Optional<LeadDto> result = service.findByEmail(email);

    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo("search@example.com");
    verify(mockRepository, times(1)).findByEmail("search@example.com");
  }

  @Test
  void shouldCreateLeadWithDefaultAddressAndUnknownPhone() {
    when(mockRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());

    ArgumentCaptor<LeadEntity> entityCaptor = ArgumentCaptor.forClass(LeadEntity.class);
    when(mockRepository.save(entityCaptor.capture()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    LeadDto result = service.addLead("test@example.com", "Company", LeadStatus.QUALIFIED);

    LeadEntity capturedEntity = entityCaptor.getValue();
    assertThat(capturedEntity.contact().email()).isEqualTo("test@example.com");
    assertThat(capturedEntity.contact().phone()).isEqualTo("Unknown");
    assertThat(capturedEntity.contact().address().city()).isEqualTo("Unknown");
    assertThat(capturedEntity.contact().address().street()).isEqualTo("Unknown");
    assertThat(capturedEntity.contact().address().zip()).isEqualTo("00000");
    assertThat(capturedEntity.company()).isEqualTo("Company");
    assertThat(capturedEntity.status()).isEqualTo("QUALIFIED");

    assertThat(result.email()).isEqualTo("test@example.com");
    assertThat(result.phone()).isEqualTo("Unknown");
    assertThat(result.company()).isEqualTo("Company");
    assertThat(result.status()).isEqualTo(LeadStatus.QUALIFIED);
  }

  @Test
  void shouldReturnOnlyNewLeadsWhenFindByStatusNew() {
    LeadRepository repository = new LeadRepository();
    LeadService leadService = new LeadService(repository);

    leadService.addLead("new1@example.com", "Corp1", LeadStatus.NEW);
    leadService.addLead("new2@example.com", "Corp2", LeadStatus.NEW);
    leadService.addLead("new3@example.com", "Corp3", LeadStatus.NEW);
    leadService.addLead("contacted1@example.com", "Corp4", LeadStatus.CONTACTED);
    leadService.addLead("contacted2@example.com", "Corp5", LeadStatus.CONTACTED);
    leadService.addLead("contacted3@example.com", "Corp6", LeadStatus.CONTACTED);
    leadService.addLead("contacted4@example.com", "Corp7", LeadStatus.CONTACTED);
    leadService.addLead("contacted5@example.com", "Corp8", LeadStatus.CONTACTED);
    leadService.addLead("qualified1@example.com", "Corp9", LeadStatus.QUALIFIED);
    leadService.addLead("qualified2@example.com", "Corp10", LeadStatus.QUALIFIED);

    List<LeadDto> result = leadService.findByStatus(LeadStatus.NEW);

    assertThat(result).hasSize(3);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.NEW));
  }

  @Test
  void shouldReturnEmptyListWhenNoLeadsWithStatus() {
    LeadRepository repository = new LeadRepository();
    LeadService leadService = new LeadService(repository);

    leadService.addLead("new1@example.com", "Corp1", LeadStatus.NEW);
    leadService.addLead("contacted1@example.com", "Corp2", LeadStatus.CONTACTED);

    List<LeadDto> result = leadService.findByStatus(LeadStatus.QUALIFIED);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldReturnOnlyContactedLeadsWhenFindByStatusContacted() {
    LeadRepository repository = new LeadRepository();
    LeadService leadService = new LeadService(repository);

    leadService.addLead("new1@example.com", "Corp1", LeadStatus.NEW);
    leadService.addLead("contacted1@example.com", "Corp2", LeadStatus.CONTACTED);
    leadService.addLead("contacted2@example.com", "Corp3", LeadStatus.CONTACTED);
    leadService.addLead("qualified1@example.com", "Corp4", LeadStatus.QUALIFIED);

    List<LeadDto> result = leadService.findByStatus(LeadStatus.CONTACTED);

    assertThat(result).hasSize(2);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
  }

  @Test
  void shouldReturnOnlyQualifiedLeadsWhenFindByStatusQualified() {
    LeadRepository repository = new LeadRepository();
    LeadService leadService = new LeadService(repository);

    leadService.addLead("new1@example.com", "Corp1", LeadStatus.NEW);
    leadService.addLead("qualified1@example.com", "Corp2", LeadStatus.QUALIFIED);
    leadService.addLead("qualified2@example.com", "Corp3", LeadStatus.QUALIFIED);
    leadService.addLead("qualified3@example.com", "Corp4", LeadStatus.QUALIFIED);

    List<LeadDto> result = leadService.findByStatus(LeadStatus.QUALIFIED);

    assertThat(result).hasSize(3);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
  }
}