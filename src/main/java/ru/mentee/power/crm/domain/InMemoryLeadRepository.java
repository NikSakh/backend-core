package ru.mentee.power.crm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryLeadRepository implements Repository<Lead> {

  private final List<Lead> internalStorage = new ArrayList<>();

  @Override
  public boolean add(Lead lead) {
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
  public Optional<Lead> findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    return internalStorage.stream()
        .filter(lead -> lead.id().equals(id))
        .findFirst();
  }

  @Override
  public List<Lead> findAll() {
    // Defensive copy: возвращаем копию списка, чтобы клиент не мог изменить внутреннее хранилище
    return new ArrayList<>(internalStorage);
  }

  private boolean contains(Lead lead) {
    return internalStorage.contains(lead);
  }
}