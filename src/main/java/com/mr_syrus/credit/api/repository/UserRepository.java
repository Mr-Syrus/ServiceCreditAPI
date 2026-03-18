package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
