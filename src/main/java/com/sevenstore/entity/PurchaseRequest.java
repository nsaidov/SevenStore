package com.sevenstore.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PurchaseRequest {
    private Long purchaseId;
    private List<PurchaseItemRequest> items = new ArrayList<>();
    private String param;
}
