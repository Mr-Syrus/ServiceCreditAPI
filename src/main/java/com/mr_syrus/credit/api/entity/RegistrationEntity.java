package com.mr_syrus.credit.api.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "registrations")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "postal_index", nullable = false, length = 6)
    private Integer postalIndex;

    @Column(name = "migration_department", nullable = false, length = 300)
    private String migrationDepartment;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "flat")
    private Integer flat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private  UserEntity user;

    public RegistrationEntity() {
    }

    public RegistrationEntity(
            LocalDate date,
            Integer postalIndex,
            String migrationDepartment,
            String region,
            String district,
            String city,
            String street,
            String house,
            Integer flat,
            UserEntity user
    ) {
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.postalIndex = Objects.requireNonNull(postalIndex, "Index cannot be null");
        this.migrationDepartment = requireNonBlank(migrationDepartment, "Migration department");
        this.region = requireNonBlank(region, "Region");
        this.district = requireNonBlank(district, "District");
        this.city = requireNonBlank(city, "City");
        this.street = requireNonBlank(street, "Street");
        this.house = Objects.requireNonNull(house, "House cannot be null");
        this.flat = flat;
        this.user = Objects.requireNonNull(user, "User cannot be null");
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }

    public Integer getId() { return id; }

    public LocalDate getDate() { return date; }

    public Integer getPostalIndex() { return postalIndex; }

    public String getMigrationDepartment() { return migrationDepartment; }

    public String getRegion() { return region; }

    public String getDistrict() { return district; }

    public String getCity() { return city; }

    public String getStreet() { return street; }

    public String getHouse() { return house; }

    public Integer getFlat() { return flat; }

    public UserEntity getUser() { return user; }

}
