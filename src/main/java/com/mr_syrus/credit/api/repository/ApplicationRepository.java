package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
}
