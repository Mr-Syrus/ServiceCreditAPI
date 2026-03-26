package com.mr_syrus.credit.api.repository;

import com.mr_syrus.credit.api.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
}
