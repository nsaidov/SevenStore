package com.sevenstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@Table(name = "purchase_items")
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    @JsonBackReference
    private Purchase purchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product")
    private Product product;

    @Column(name = "quantity", precision = 15, scale = 3)
    private BigDecimal quantity;

    @Column
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit")
    private Unit unit;
}
