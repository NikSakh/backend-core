package ru.mentee.power.crm.domain.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "deals")
public class DealJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(length = 3)
  private String currency;

  @Column(nullable = false)
  private String stage;

  @Column
  private Integer probability;

  @Column(name = "expected_close_date")
  private LocalDate expectedCloseDate;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DealProduct> dealProducts = new ArrayList<>();

  protected DealJpaEntity() {
  }

  public DealJpaEntity(String title, BigDecimal amount, String stage) {
    this.title = title;
    this.amount = amount;
    this.currency = "USD";
    this.stage = stage;
    this.probability = 0;
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public String getStage() {
    return stage;
  }

  public Integer getProbability() {
    return probability;
  }

  public LocalDate getExpectedCloseDate() {
    return expectedCloseDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public List<DealProduct> getDealProducts() {
    return dealProducts;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public void setProbability(Integer probability) {
    this.probability = probability;
  }

  public void setExpectedCloseDate(LocalDate expectedCloseDate) {
    this.expectedCloseDate = expectedCloseDate;
  }

  public void addDealProduct(DealProduct dealProduct) {
    dealProducts.add(dealProduct);
    dealProduct.setDeal(this);
  }

  public void removeDealProduct(DealProduct dealProduct) {
    dealProducts.remove(dealProduct);
    dealProduct.setDeal(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealJpaEntity that = (DealJpaEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
