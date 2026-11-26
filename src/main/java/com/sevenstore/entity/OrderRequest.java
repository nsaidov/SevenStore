package com.sevenstore.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    Long orderId;
    String name;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private List<OrderItemRequest> items = new ArrayList<>();
    private String param;
}
