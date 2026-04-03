package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<UserEntity> users = new ArrayList<>();

    protected RoleEntity() {
    }

    public RoleEntity(String name, String description, Boolean active) {
        setName(name);
        setDescription(description);
        setActive(active);
    }

    public RoleEntity(String name) {
        this(name, null, true);
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Role name cannot be empty");
        }
        if (name.length() > 50) {
            throw new IllegalStateException("Role name must be less than 50 characters");
        }
        if (!name.matches("^[A-Z_]+$")) {
            throw new IllegalStateException("Role name must contain only uppercase letters and underscores");
        }
        if (active == null) {
            throw new IllegalStateException("Active flag cannot be null");
        }
    }

    public void addUser(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.add(user);
        if (user.getRole() != this) {
            user.setRole(this);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setDescription(String description) {
        this.description = (description != null && !description.isBlank()) ? description : null;
    }

    public void setActive(Boolean active) {
        if (active == null) {
            throw new IllegalArgumentException("Active flag cannot be null");
        }
        this.active = active;
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Role name must be less than 50 characters");
        }
        if (!name.matches("^[A-Z_]+$")) {
            throw new IllegalArgumentException("Role name must contain only uppercase letters and underscores");
        }
        this.name = name;
    }
}