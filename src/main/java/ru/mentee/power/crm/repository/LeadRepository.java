package ru.mentee.power.crm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import ru.mentee.power.crm.model.Lead;

public class LeadRepository {
  private final ConcurrentHashMap<String, Lead> storage = new ConcurrentHashMap<>();

  public Lead save(Lead lead) {
    storage.put(lead.id(), lead);
    return lead;
  }

  public Optional<Lead> findById(String id) {
    Lead lead = storage.get(id);
    return Optional.ofNullable(lead);
  }

  public Optional<Lead> findByEmail(String email) {
    return storage.values().stream()
        .filter(lead -> email.equals(lead.email()))
        .findFirst();
  }

  public List<Lead> findAll() {
    return new ArrayList<>(storage.values());
  }
}