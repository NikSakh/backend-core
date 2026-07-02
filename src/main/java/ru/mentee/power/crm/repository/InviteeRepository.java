package ru.mentee.power.crm.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ru.mentee.power.crm.domain.Invitee;

public class InviteeRepository {
  public List<Invitee> findAll() { return List.of(); }
  public Optional<Invitee> findById(UUID id) { return Optional.empty(); }
  public Invitee save(Invitee invitee) { return invitee; }
  public void delete(Invitee invitee) {}
}