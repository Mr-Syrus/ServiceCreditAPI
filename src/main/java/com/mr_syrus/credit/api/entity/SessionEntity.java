package com.mr_syrus.credit.api.entity;


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
    private UserEntity user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "ip_address")
    private String ipAddress;

    // User-Agent браузера/клиента
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    // Статус сессии
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;

    // Автоматическая установка даты создания
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (expiresAt == null) {
            expiresAt = createdAt.plusHours(2);
        }
        if (status == null) {
            status = SessionStatus.ACTIVE;
        }
    }

    // Метод для обновления активности
    public void updateActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    // Проверка, активна ли сессия
    public boolean isValid() {
        return status == SessionStatus.ACTIVE &&
                LocalDateTime.now().isBefore(expiresAt);
    }

    public SessionEntity(String sessionKey, UserEntity user, String ipAddress, String userAgent) {
        this.sessionKey = sessionKey;
        this.user = user;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public UserEntity getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public SessionStatus getStatus() {
        return status;
    }
}