package ru.mentee.power.crm.spring.rest.fixed;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mentee.power.crm.spring.rest.fixed.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.rest.fixed.dto.InviteeResponse;

public class InviteeService {

  public Page<InviteeResponse> getAll(Pageable pageable) {
    return Page.empty();
  }

  public InviteeResponse getById(UUID id) {
    throw new RuntimeException("Not implemented");
  }

  public InviteeResponse create(CreateInviteeRequest request) {
    throw new RuntimeException("Not implemented");
  }

  public InviteeResponse updateStatus(UUID id, String status) {
    throw new RuntimeException("Not implemented");
  }

  public void delete(UUID id) {
    throw new RuntimeException("Not implemented");
  }
}
