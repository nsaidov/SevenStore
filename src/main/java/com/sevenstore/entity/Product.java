package com.sevenstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "unit_sale", nullable = false)
    @ManyToOne
    private Unit unitSale;

    @JoinColumn(name = "unit_purchase", nullable = false)
    @ManyToOne
    private Unit unitPurchase;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, unique = true)
    private String code;
}
