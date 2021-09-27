package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}