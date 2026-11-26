package com.sevenstore.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    Long product_id;
    Long shop_id;
    Long unit_id;
    BigDecimal quantity;
}
