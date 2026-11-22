package com.sevenstore.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderRequest {
    String name;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private Long version;
    private List<OrderItemRequest> items = new ArrayList<>();
}
