package ru.mentee.power.crm.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

  boolean add(T entity);

  boolean remove(UUID id);

  Optional<T> findById(UUID id);

  List<T> findAll();
}