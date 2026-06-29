package ru.mentee.power.crm.spring.exception;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String entityType, String entityId) {
    super(entityType + " not found with id: " + entityId);
  }
}
