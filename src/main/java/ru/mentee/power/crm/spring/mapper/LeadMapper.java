package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.mentee.power.crm.model.LeadDto;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;

@Mapper
public interface LeadMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "rejectionReasonId", ignore = true)
  @Mapping(target = "rejectionReasonName", ignore = true)
  LeadDto toEntity(CreateLeadRequest request);

  LeadResponse toResponse(LeadDto lead);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "rejectionReasonId", ignore = true)
  @Mapping(target = "rejectionReasonName", ignore = true)
  void updateEntity(UpdateLeadRequest request, @MappingTarget LeadDto lead);
}
