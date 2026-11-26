package com.sevenstore.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseItemRequest {
    Long product_id;
    Long unit_id;
    BigDecimal quantity;
    Long price;
}
