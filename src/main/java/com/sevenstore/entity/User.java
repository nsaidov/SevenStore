package com.sevenstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column()
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
