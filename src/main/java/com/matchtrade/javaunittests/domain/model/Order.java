package com.matchtrade.javaunittests.domain.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {
  @Id private String id;
  private String userId;
  @ElementCollection private List<String> productIds;
  private OrderStatus status;
}
