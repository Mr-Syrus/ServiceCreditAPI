package com.mr_syrus.credit.api.entity;


import com.mr_syrus.credit.api.util.TokenGeneratorUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class SessionEntity {

    @Id
    @Column(name = "session_key", nullable = false, length = 500)
    private String sessionKey;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "ip_address")
    private String ipAddress;

    // User-Agent браузера/клиента
    @Column(name = "user_agent", length = 500)
    private String userAgent;


    //конструктор
    public SessionEntity(UserEntity userId, String ipAddress, String userAgent) {
        this.sessionKey = TokenGeneratorUtil.generateSessionKey(128);
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public UserEntity getUser() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getIpAddress() { return ipAddress; }

    public String getUserAgent() { return userAgent; }


    // Автоматическая установка даты создания
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = createdAt.plusHours(2);
    }


}