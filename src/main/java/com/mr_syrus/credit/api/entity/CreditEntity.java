package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "credits")
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    public CreditEntity() {
    }

    public CreditEntity(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Credit name cannot be empty");
        }

        if (name.length() > 20) {
            throw new IllegalArgumentException("Credit name must be less than 20 characters");
        }

        if (name.length() < 3) {
            throw new IllegalArgumentException("Credit name must be at least 3 characters");
        }

        if (!name.matches("^[а-яА-Яa-zA-Z0-9\\s-]+$")) {
            throw new IllegalArgumentException("Credit name can only contain letters, numbers, spaces and hyphens");
        }

        this.name = name.trim();
    }
}