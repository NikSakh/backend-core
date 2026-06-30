package ru.mentee.power.crm.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "rejection_reasons")
public class RejectionReasons {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Boolean active = true;

  protected RejectionReasons() {}

  public RejectionReasons(String name) {
    this.name = name;
    this.active = true;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Boolean getActive() {
    return active;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
