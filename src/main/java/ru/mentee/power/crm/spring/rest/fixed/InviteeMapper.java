package ru.mentee.power.crm.spring.rest.fixed;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mentee.power.crm.domain.Invitee;
import ru.mentee.power.crm.spring.rest.fixed.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.rest.fixed.dto.InviteeResponse;

@Mapper(componentModel = "spring")
public interface InviteeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  Invitee toEntity(CreateInviteeRequest request);

  InviteeResponse toResponse(Invitee entity);
}
