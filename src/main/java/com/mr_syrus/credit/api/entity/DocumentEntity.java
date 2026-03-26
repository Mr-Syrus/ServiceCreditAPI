package com.mr_syrus.credit.api.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //паспортные данные
    @Column(name = "passport_series", nullable = false, length = 4)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false, length = 6)
    private String passportNumber;

    @Column(name = "passport_issued_by", nullable = false, columnDefinition = "TEXT")
    private String passportIssuedBy;

    @Column(name = "department_code", nullable = false, length = 7)
    private String departmentCode;

    @Column(name = "passport_issue_date", nullable = false)
    private LocalDate passportIssueDate;

    //личностные данные

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @Column(name = "middle_name", length = 64)
    private String middleName;

    @Column(name = "gender", )
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;





    @Column(name = "registration_address", nullable = false, columnDefinition = "TEXT")
    private String registrationAddress;



    @Column(name = "workplace", nullable = false,)


    public DocumentEntity() {
    }

    public DocumentEntity(

    )
}