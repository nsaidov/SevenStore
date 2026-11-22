package com.sevenstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

//@Entity
//@Setter
//@Getter
//@Table(name = "order_items")
//public class OrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product", nullable = false)
//    private Product product;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shop", nullable = false)
//    private Shop shop;
//
//    @Column(name = "quantity", nullable = false)
//    private BigDecimal quantity;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "unit", nullable = false)
//    private Unit unit;
//}

@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop")
    private Shop shop;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit")
    private Unit unit;

}