package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.PersonalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalDataEntity, Integer> {
}
