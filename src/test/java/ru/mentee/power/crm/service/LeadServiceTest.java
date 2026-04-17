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
    LeadEntity existingLead = new LeadEntity(existingId, contact, "Existing Company", "CONTACTED");

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
    LeadEntity entityFirst = new LeadEntity(UUID.randomUUID(), contactFirst, "Company First", "NEW");

    Address addressSecond = new Address("CitySecond", "StreetSecond", "22222");
    Contact contactSecond = new Contact("leadSecond@example.com", "+222222222", addressSecond);
    LeadEntity entitySecond = new LeadEntity(UUID.randomUUID(), contactSecond, "Company Second", "CONTACTED");

    List<LeadEntity> entities = List.of(entityFirst, entitySecond);
    when(mockRepository.findAll()).thenReturn(entities);

    List<LeadDto> result = service.findAll();

    assertThat(result).hasSize(2);
    assertThat(result.get(0).email()).isEqualTo("leadFirst@example.com");
    assertThat(result.get(0).company()).isEqualTo("Company First");
    assertThat(result.get(0).status()).isEqualTo(LeadStatus.NEW);
    assertThat(result.get(1).email()).isEqualTo("leadSecond@example.com");
    assertThat(result.get(1).company()).isEqualTo("Company Second");
    assertThat(result.get(1).status()).isEqualTo(LeadStatus.CONTACTED);
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
}