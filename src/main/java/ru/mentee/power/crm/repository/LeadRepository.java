package ru.mentee.power.crm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import ru.mentee.power.crm.domain.LeadEntity;

public class LeadRepository {
  private final ConcurrentHashMap<String, LeadEntity> storage = new ConcurrentHashMap<>();

  public LeadEntity save(LeadEntity lead) {
    storage.put(lead.id().toString(), lead);
    return lead;
  }

  public Optional<LeadEntity> findById(String id) {
    LeadEntity lead = storage.get(id);
    return Optional.ofNullable(lead);
  }

  public Optional<LeadEntity> findByEmail(String email) {
    return storage.values().stream()
        .filter(lead -> email.equals(lead.contact().email()))
        .findFirst();
  }

  public List<LeadEntity> findAll() {
    return new ArrayList<>(storage.values());
  }
}