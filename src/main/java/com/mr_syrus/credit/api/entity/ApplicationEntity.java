package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "applications")
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "passport_id", nullable = false)
    private PersonalDataEntity passport;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.ON_REVIEW;

    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private CreditEntity credit;

    @Column(name = "credit_term", nullable = false)
    private Integer creditTerm;

    @Column(name = "credit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal creditAmount;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    public ApplicationEntity(){
    }

    public ApplicationEntity(
            UserEntity user,
            PersonalDataEntity passport,
            CreditEntity credit,
            Integer creditTerm,
            BigDecimal creditAmount) {
        this.user = Objects.requireNonNull(user, "User cannot be null");
        this.passport = Objects.requireNonNull(passport, "Passport cannot be null");
        this.credit = Objects.requireNonNull(credit, "Credit cannot be null");

        if (creditTerm == null || creditTerm <= 0) {
            throw new IllegalArgumentException("Credit term must be positive");
        }
        this.creditTerm = creditTerm;

        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.creditAmount = creditAmount;
    }

    @PrePersist
    private void prePersist() {
        if (user == null) {
            throw new IllegalStateException("User cannot be null");
        }
        if (passport == null) {
            throw new IllegalStateException("Passport cannot be null");
        }
        if (credit == null) {
            throw new IllegalStateException("Credit cannot be null");
        }
        if (creditTerm == null || creditTerm <= 0) {
            throw new IllegalStateException("Credit term must be positive");
        }
        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Credit amount must be positive");
        }
        if (status == null) {
            throw new IllegalStateException("Status cannot be null");
        }

        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public PersonalDataEntity getPassport() {
        return passport;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (this.status == ApplicationStatus.APPROVED || this.status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Cannot change status of completed application");
        }
        this.status = newStatus;
        if (newStatus == ApplicationStatus.APPROVED || newStatus == ApplicationStatus.REJECTED) {
            this.completionDate = LocalDateTime.now();
        }
    }

    public CreditEntity getCredit() {
        return credit;
    }

    public Integer getCreditTerm() {
        return creditTerm;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }
}