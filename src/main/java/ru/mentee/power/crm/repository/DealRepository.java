package ru.mentee.power.crm.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;

@Repository
public class DealRepository {
  private final Map<UUID, Deal> storage = new ConcurrentHashMap<>();

  public void save(Deal deal) {
    storage.put(deal.getId(), deal);
  }

  public Optional<Deal> findById(UUID id) {
    return Optional.ofNullable(storage.get(id));
  }

  public List<Deal> findAll() {
    return List.copyOf(storage.values());
  }

  public List<Deal> findByStatus(DealStatus status) {
    return storage.values().stream()
        .filter(deal -> deal.getStatus() == status)
        .collect(Collectors.toList());
  }

  public void deleteById(UUID id) {
    storage.remove(id);
  }
}