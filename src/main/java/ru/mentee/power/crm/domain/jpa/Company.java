package ru.mentee.power.crm.domain.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column private String industry;

  @OneToMany(mappedBy = "companyRef", cascade = CascadeType.PERSIST)
  private List<LeadJpaEntity> leads = new ArrayList<>();

  protected Company() {}

  public Company(String name, String industry) {
    this.name = name;
    this.industry = industry;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getIndustry() {
    return industry;
  }

  public List<LeadJpaEntity> getLeads() {
    return leads;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public void addLead(LeadJpaEntity lead) {
    leads.add(lead);
    lead.setCompanyRef(this);
  }

  public void removeLead(LeadJpaEntity lead) {
    leads.remove(lead);
    lead.setCompanyRef(null);
  }
}
