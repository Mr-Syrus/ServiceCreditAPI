package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

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

    // Паспортные данные
    @Column(name = "passport_series", nullable = false, length = 4)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false, length = 6)
    private String passportNumber;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "passport_issue_date", nullable = false)
    private LocalDate passportIssueDate;

    @Column(name = "passport_issued_by", nullable = false, columnDefinition = "TEXT")
    private String passportIssuedBy;

    @Column(name = "registration_address", nullable = false, columnDefinition = "TEXT")
    private String registrationAddress;

    @Column(name = "department_code", nullable = false, length = 7)
    private String departmentCode;

    public ApplicationEntity(){
    }


    public ApplicationEntity(
            UserEntity user,
            CreditEntity credit,
            Integer creditTerm,
            BigDecimal creditAmount,
            String passportSeries,
            String passportNumber,
            LocalDate birthDate,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String registrationAddress,
            String departmentCode) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (credit == null) {
            throw new IllegalArgumentException("Credit cannot be null");
        }
        if (creditTerm == null || creditTerm <= 0) {
            throw new IllegalArgumentException("Credit term must be positive");
        }
        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        if (passportSeries == null || !passportSeries.matches("\\d{4}")) {
            throw new IllegalArgumentException("Passport series must be 4 digits");
        }
        if (passportNumber == null || !passportNumber.matches("\\d{6}")) {
            throw new IllegalArgumentException("Passport number must be 6 digits");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (passportIssueDate == null) {
            throw new IllegalArgumentException("Passport issue date cannot be null");
        }
        if (passportIssuedBy == null || passportIssuedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Passport issued by cannot be empty");
        }
        if (registrationAddress == null || registrationAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration address cannot be empty");
        }
        if (departmentCode == null || !departmentCode.matches("\\d{3}-\\d{3}")) {
            throw new IllegalArgumentException("Department code must be in format '000-000'");
        }

        this.user = user;
        this.credit = credit;
        this.creditTerm = creditTerm;
        this.creditAmount = creditAmount;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.birthDate = birthDate;
        this.passportIssueDate = passportIssueDate;
        this.passportIssuedBy = passportIssuedBy;
        this.registrationAddress = registrationAddress;
        this.departmentCode = departmentCode;
    }

    public Integer getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public ApplicationStatus getStatus() {
        return status;
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

    public String getPassportSeries() {
        return passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public String getPassportIssuedBy() {
        return passportIssuedBy;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    // Автоматическая установка даты создания заявки
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        status = ApplicationStatus.ON_REVIEW;
    }

    // Автоматическая дата завершения заявки
    public void setStatus(ApplicationStatus newStatus) {
        if (this.status == ApplicationStatus.APPROVED || this.status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Нельзя изменить статус завершённой заявки");
        }

        this.status = newStatus;

        if (newStatus == ApplicationStatus.APPROVED || newStatus == ApplicationStatus.REJECTED) {
            this.completionDate = LocalDateTime.now();
        }
    }
}