package ru.mentee.power.crm.model;

public enum LeadStatus {
  NEW("Новый"),
  CONTACTED("В контакте"),
  QUALIFIED("Квалифицирован"),
  CONVERTED("Конвертирован"),
  LOST("Потерян");

  private final String displayName;

  LeadStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}