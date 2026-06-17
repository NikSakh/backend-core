package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;

@Mapper(componentModel = "spring")
public interface LeadMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "rejectionReasonId", ignore = true)
  LeadDto toEntity(CreateLeadRequest request);

  LeadResponse toResponse(LeadDto dto);

  @Mapping(target = "id", ignore = true)
  LeadDto toEntity(UpdateLeadRequest request);
}
