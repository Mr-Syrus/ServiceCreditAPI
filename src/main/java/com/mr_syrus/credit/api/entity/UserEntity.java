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

    @Column(name = "phone_number", nullable = false, length = 11, unique = true)
    private String phone;

    //статус
    @Enumerated(EnumType.STRING)
    @Column(name = "rosfinmonitoring_status", nullable = false)
    private RosfinmonitoringStatus rosfinmonitoringStatus = RosfinmonitoringStatus.NOT_RESTRICTED;

    @Column(name = "deactivated", nullable = false)
    private Boolean deactivated = false;






    protected UserEntity() {
    }

    public UserEntity(
            String username,
            String mail,
            String passwordHash,
            String phone,
            String firstName,
            String lastName,
            String middleName,
            String passportSeries,
            String passportNumber,
            LocalDate birthDate,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String registrationAddress,
            String departmentCode
    ) {
        setUsername(username);
        setMail(mail);
        setPasswordHash(passwordHash);
        setPhone(phone);
        setFirstName(firstName);
        setLastName(lastName);
        setMiddleName(middleName);
        setPassportSeries(passportSeries);
        setPassportNumber(passportNumber);
        setBirthDate(birthDate);
        setPassportIssueDate(passportIssueDate);
        setPassportIssuedBy(passportIssuedBy);
        setRegistrationAddress(registrationAddress);
        setDepartmentCode(departmentCode);
    }



    public Integer getId() { return id; }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public RosfinmonitoringStatus getRosfinmonitoringStatus() { return rosfinmonitoringStatus; } //пересмотреть в случае декомпозиции микросервиса

    public Boolean getDeactivated() { return deactivated; } //пересмотреть в случае декомпозиции микросервиса

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
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

    //захешировать пароль и пересмотреть это
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        // Убираем все нецифровые символы для проверки
        String cleanPhone = phone.replaceAll("[^\\d]", "");
        if (!cleanPhone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone must contain 11 digits");
        }
        this.phone = cleanPhone;
    }

    public void setRosfinmonitoringStatus(RosfinmonitoringStatus rosfinmonitoringStatus) {
        if (rosfinmonitoringStatus == null) {
            throw new IllegalArgumentException("Rosfinmonitoring status cannot be null");
        }
        this.rosfinmonitoringStatus = rosfinmonitoringStatus;
    }

    public void setDeactivated(Boolean deactivated) {
        if (deactivated == null) {
            throw new IllegalArgumentException("Deactivated status cannot be null");
        }
        this.deactivated = deactivated;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (firstName.length() > 64) {
            throw new IllegalArgumentException("First name must be less than 64 characters");
        }
        if (!firstName.matches("^[а-яА-Яa-zA-Z\\s-]+$")) {
            throw new IllegalArgumentException("First name can only contain letters, spaces and hyphens");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (lastName.length() > 64) {
            throw new IllegalArgumentException("Last name must be less than 64 characters");
        }
        if (!lastName.matches("^[а-яА-Яa-zA-Z\\s-]+$")) {
            throw new IllegalArgumentException("Last name can only contain letters, spaces and hyphens");
        }
        this.lastName = lastName.trim();
    }

    public void setMiddleName(String middleName) {
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (middleName.length() > 64) {
                throw new IllegalArgumentException("Middle name must be less than 64 characters");
            }
            if (!middleName.matches("^[а-яА-Яa-zA-Z\\s-]+$")) {
                throw new IllegalArgumentException("Middle name can only contain letters, spaces and hyphens");
            }
            this.middleName = middleName.trim();
        } else {
            this.middleName = null;
        }
    }

    public void setPassportSeries(String passportSeries) {
        if (passportSeries == null || passportSeries.trim().isEmpty()) {
            throw new IllegalArgumentException("Passport series cannot be empty");
        }
        String cleanSeries = passportSeries.replaceAll("[^\\d]", "");
        if (!cleanSeries.matches("\\d{4}")) {
            throw new IllegalArgumentException("Passport series must contain 4 digits");
        }
        this.passportSeries = cleanSeries;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Passport number cannot be empty");
        }
        String cleanNumber = passportNumber.replaceAll("[^\\d]", "");
        if (!cleanNumber.matches("\\d{6}")) {
            throw new IllegalArgumentException("Passport number must contain 6 digits");
        }
        this.passportNumber = cleanNumber;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("User must be at least 18 years old");
        }
        if (birthDate.isBefore(LocalDate.now().minusYears(150))) {
            throw new IllegalArgumentException("Invalid birth date");
        }
        this.birthDate = birthDate;
    }

    public void setPassportIssueDate(LocalDate passportIssueDate) {
        if (passportIssueDate == null) {
            throw new IllegalArgumentException("Passport issue date cannot be null");
        }
        if (passportIssueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Passport issue date cannot be in the future");
        }
        if (birthDate != null && passportIssueDate.isBefore(birthDate.plusYears(14))) {
            throw new IllegalArgumentException("Passport cannot be issued before 14 years old");
        }
        this.passportIssueDate = passportIssueDate;
    }

    public void setPassportIssuedBy(String passportIssuedBy) {
        if (passportIssuedBy == null || passportIssuedBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Passport issued by cannot be empty");
        }
        if (passportIssuedBy.length() > 1000) {
            throw new IllegalArgumentException("Passport issued by is too long");
        }
        this.passportIssuedBy = passportIssuedBy.trim();
    }

    public void setRegistrationAddress(String registrationAddress) {
        if (registrationAddress == null || registrationAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration address cannot be empty");
        }
        if (registrationAddress.length() > 1000) {
            throw new IllegalArgumentException("Registration address is too long");
        }
        this.registrationAddress = registrationAddress.trim();
    }

    public void setDepartmentCode(String departmentCode) {
        if (departmentCode == null || departmentCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Department code cannot be empty");
        }
        String cleanCode = departmentCode.replaceAll("[^\\d]", "");
        if (!cleanCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Department code must contain 6 digits");
        }
        // Сохраняем в формате "000-000"
        this.departmentCode = cleanCode.substring(0, 3) + "-" + cleanCode.substring(3, 6);
    }

    // Метод для обновления паспортных данных (например, при замене паспорта)
    public void updatePassportData(
            String passportSeries,
            String passportNumber,
            LocalDate passportIssueDate,
            String passportIssuedBy,
            String departmentCode
    ) {
        setPassportSeries(passportSeries);
        setPassportNumber(passportNumber);
        setPassportIssueDate(passportIssueDate);
        setPassportIssuedBy(passportIssuedBy);
        setDepartmentCode(departmentCode);
    }
}