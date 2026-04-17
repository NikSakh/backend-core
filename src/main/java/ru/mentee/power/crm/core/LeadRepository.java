package ru.mentee.power.crm.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import ru.mentee.power.crm.domain.LeadEntity;

public class LeadRepository {
  private final Set<LeadEntity> leads = new HashSet<>();

  public LeadEntity save(LeadEntity entity) {
    leads.add(entity);
    return entity;
  }

  public Set<LeadEntity> findAll() {
    return Collections.unmodifiableSet(leads);
  }

  Optional<LeadEntity> findById(UUID id) {
    return leads.stream()
        .filter(lead -> lead.id().equals(id))
        .findFirst();
  }

  Optional<LeadEntity> findByEmail(String email) {
    return leads.stream()
        .filter(lead -> lead.contact().email().equalsIgnoreCase(email))
        .findFirst();
  }

  public boolean add(LeadEntity lead) {
    return leads.add(lead);
  }

  public boolean contains(LeadEntity lead) {
    return leads.contains(lead);
  }

  public int size() {
    return leads.size();
  }
}