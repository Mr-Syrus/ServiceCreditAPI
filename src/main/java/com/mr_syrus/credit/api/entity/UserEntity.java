package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "mail", nullable = false, length = 254, unique = true)
    private String mail;

    @Column(name = "password_hash", nullable = false, length = 64)
    private String passwordHash;

    @Column(name = "phone", nullable = false, length = 11, unique = true)
    private String phone;

    @Column(name = "deactivated", nullable = false)
    private Boolean deactivated = false;

    protected UserEntity() {
    }

    public UserEntity(
            String username,
            String mail,
            String passwordHash,
            String phone,
            Boolean deactivated

    ) {
        setUsername(username);
        setMail(mail);
        setPasswordHash(passwordHash);
        setPhone(phone);


    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalStateException("Username cannot be empty");
        }
        if (username.length() > 50) {
            throw new IllegalStateException("Username must be less than 50 characters");
        }

        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalStateException("Email cannot be empty");
        }
        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalStateException("Invalid email format");
        }
        if (mail.length() > 254) {
            throw new IllegalStateException("Email must be less than 254 characters");
        }

        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalStateException("Password hash cannot be empty");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalStateException("Phone cannot be empty");
        }

        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalStateException("Phone cannot be empty");
        }
        String cleanPhone = phone.replaceAll("[^\\d]", "");
        if (!cleanPhone.matches("\\d{11}")) {
            throw new IllegalStateException("Phone must contain 11 digits");
        }

        if (deactivated == null) {
            throw new IllegalStateException("Deactivated status cannot be null");
        }
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username must be less than 50 characters");
        }
        this.username = username;
    }

    public void setMail(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!mail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (mail.length() > 254) {
            throw new IllegalArgumentException("Email must be less than 254 characters");
        }
        this.mail = mail;
    }


    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        String cleanPhone = phone.replaceAll("[^\\d]", "");
        if (!cleanPhone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone must contain 11 digits");
        }
        this.phone = cleanPhone;
    }

    public void setDeactivated(Boolean deactivated) {
        if (deactivated == null) {
            throw new IllegalArgumentException("Deactivated status cannot be null");
        }
        this.deactivated = deactivated;
    }

    public Integer getId() { return id; }

    public String getUsername() { return username; }

    public String getMail() { return mail; }

    public String getPasswordHash() { return passwordHash; }

    public String getPhone() { return phone; }

    public Boolean getDeactivated() { return deactivated; }

}