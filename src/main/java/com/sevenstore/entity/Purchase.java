package com.sevenstore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "purchases")
public class Purchase {
    public Purchase(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Column()
    private Long version;

    @OneToMany(mappedBy = "purchaseId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurchaseItem> items = new ArrayList<>();
}
