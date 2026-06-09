package ru.mentee.power.crm.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.UUID;

@Entity
@Table(name = "leads")
public class LeadJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column
  private String phone;

  @Column
  private String company;

  @Column(nullable = false)
  private String status;

  @Version
  @Column(nullable = false)
  private Long version;

  protected LeadJpaEntity() {
  }

  public LeadJpaEntity(String email, String company, String status) {
    this.name = "Unknown";
    this.email = email;
    this.phone = "Unknown";
    this.company = company;
    this.status = status;
  }

  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getPhone() { return phone; }
  public String getCompany() { return company; }
  public String getStatus() { return status; }
  public Long getVersion() { return version; }

  public void setName(String name) { this.name = name; }
  public void setEmail(String email) { this.email = email; }
  public void setPhone(String phone) { this.phone = phone; }
  public void setCompany(String company) { this.company = company; }
  public void setStatus(String status) { this.status = status; }
}