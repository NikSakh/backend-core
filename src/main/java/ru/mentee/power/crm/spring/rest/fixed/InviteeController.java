package ru.mentee.power.crm.spring.rest.fixed;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentee.power.crm.spring.rest.fixed.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.rest.fixed.dto.InviteeResponse;
import ru.mentee.power.crm.spring.rest.fixed.dto.UpdateStatusRequest;

@RestController
@RequestMapping("/invitees")
public class InviteeController {

  private final InviteeService inviteeService;

  public InviteeController(InviteeService inviteeService) {
    this.inviteeService = inviteeService;
  }

  @GetMapping
  public ResponseEntity<Page<InviteeResponse>> getInvitees(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<InviteeResponse> page = inviteeService.getAll(pageable);
    return ResponseEntity.ok(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InviteeResponse> getById(@PathVariable UUID id) {
    InviteeResponse response = inviteeService.getById(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<InviteeResponse> create(@Valid @RequestBody CreateInviteeRequest request) {
    InviteeResponse created = inviteeService.create(request);
    URI location = URI.create("/invitees/" + created.id());
    return ResponseEntity.created(location).body(created);
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<InviteeResponse> updateStatus(
      @PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest request) {
    InviteeResponse updated = inviteeService.updateStatus(id, request.status());
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    inviteeService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
