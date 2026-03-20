package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(
            "SELECT u FROM UserEntity u " +
            "WHERE u.passportSeries = :passport_series " +
            "AND u.passportNumber = :passport_number"
    )
    Optional<UserEntity> findBySeriesAndNumber( //NullPointerException
            @Param("passport_series") String series,
            @Param("passport_number") String number
    );
}
