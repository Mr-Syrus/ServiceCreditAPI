package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.PersonalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;

import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalDataEntity, Integer> {
    @Query("SELECT pd FROM PersonalDataEntity pd " +
            "JOIN pd.user u WHERE u.email = :email " +
            "AND pd.passportSeries = :series " +
            "AND pd.passportNumber = :number AND pd.active = true")
    Optional<PersonalDataEntity> findActiveByEmailAndPassportData(
            @Param("email") String email,
            @Param("passport_series") String series,
            @Param("passport_number") String number);
}
