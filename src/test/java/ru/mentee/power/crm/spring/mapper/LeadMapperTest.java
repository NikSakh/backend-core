package ru.mentee.power.crm.spring.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;

@SpringBootTest
@ActiveProfiles("test")
class LeadMapperTest {

  @Autowired private LeadMapper leadMapper;

  @Test
  void shouldMapCreateRequestToEntity() {
    CreateLeadRequest request = new CreateLeadRequest("test@example.com", "Corp", "NEW");
    request.phone("+123");

    LeadDto entity = leadMapper.toEntity(request);

    assertThat(entity).isNotNull();
    assertThat(entity.id()).isNull();
    assertThat(entity.email()).isEqualTo("test@example.com");
    assertThat(entity.phone()).isEqualTo("+123");
    assertThat(entity.company()).isEqualTo("Corp");
    assertThat(entity.status()).isEqualTo(LeadStatus.NEW);
  }

  @Test
  void shouldMapEntityToResponse() {
    UUID id = UUID.randomUUID();
    LeadDto entity = new LeadDto(id.toString(), "test@example.com", "+123", "Corp", LeadStatus.NEW);

    LeadResponse response = leadMapper.toResponse(entity);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(id);
    assertThat(response.getEmail()).isEqualTo("test@example.com");
    assertThat(response.getCompany()).isEqualTo("Corp");
    assertThat(response.getStatus()).isEqualTo("NEW");
  }
}
