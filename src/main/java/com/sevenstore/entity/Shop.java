package com.sevenstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "shops")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "sort_order")
    private Long sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
