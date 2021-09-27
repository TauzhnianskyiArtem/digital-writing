package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
