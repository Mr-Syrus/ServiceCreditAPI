package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "authorisation_codes")
public class AuthorizationCodeEntity {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name = "code", nullable = false, length = 6)
    private String code;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTameStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTameEnd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    protected AuthorizationCodeEntity() {
    }

    public AuthorizationCodeEntity(String code, UserEntity user) {
        this.code = code;
        this.user = user;
    }

    public UUID getId() { return id; }

    public String getCode() { return code; }

    public LocalDateTime getDateTameStart() { return dateTameStart; }

    public LocalDateTime getDateTameEnd() { return dateTameEnd; }

    public UserEntity getUserId() { return user; }

    @PrePersist
    protected void onCreate() {
        dateTameStart = LocalDateTime.now();
        dateTameEnd = dateTameEnd.plusMinutes(3);
    }



}
