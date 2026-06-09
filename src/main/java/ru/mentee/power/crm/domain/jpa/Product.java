package ru.mentee.power.crm.domain.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  private String sku;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private Boolean active = true;

  protected Product() {
  }

  public Product(String name, String sku, BigDecimal price, Boolean active) {
    this.name = name;
    this.sku = sku;
    this.price = price;
    this.active = active;
  }

  public UUID getId() { return id; }
  public String getName() { return name; }
  public String getSku() { return sku; }
  public BigDecimal getPrice() { return price; }
  public Boolean getActive() { return active; }

  public void setName(String name) { this.name = name; }
  public void setSku(String sku) { this.sku = sku; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public void setActive(Boolean active) { this.active = active; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(sku, product.sku);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sku);
  }
}