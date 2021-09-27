package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<SystemEntity, Long> {
}