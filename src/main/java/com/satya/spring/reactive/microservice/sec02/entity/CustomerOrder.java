package com.satya.spring.reactive.microservice.sec02.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("customer_order")
public class CustomerOrder {
  @Id private UUID orderId;
  private int customerId;
  private int productId;
  private int amount;
  private Instant orderDate;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public Instant getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Instant orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public String toString() {
    return "CustomerOrder{"
        + "orderId="
        + orderId
        + ", customerId="
        + customerId
        + ", productId="
        + productId
        + ", amount="
        + amount
        + ", orderDate="
        + orderDate
        + '}';
  }
}
