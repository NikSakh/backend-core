package ru.mentee.power.crm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLeadRepository implements Repository<LeadEntity> {

  private final List<LeadEntity> internalStorage = new ArrayList<>();

  @Override
  public boolean add(LeadEntity lead) {
    if (lead == null) {
      throw new IllegalArgumentException("Lead cannot be null");
    }
    if (contains(lead)) {
      return false;
    }
    internalStorage.add(lead);
    return true;
  }

  @Override
  public boolean remove(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    return internalStorage.removeIf(lead -> lead.id().equals(id));
  }

  @Override
  public Optional<LeadEntity> findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    return internalStorage.stream()
        .filter(lead -> lead.id().equals(id))
        .findFirst();
  }

  @Override
  public List<LeadEntity> findAll() {
    return new ArrayList<>(internalStorage);
  }

  private boolean contains(LeadEntity lead) {
    return internalStorage.contains(lead);
  }
}