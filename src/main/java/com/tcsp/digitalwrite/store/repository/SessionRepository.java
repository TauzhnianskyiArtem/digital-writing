package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {

    SessionEntity findByUser(UserEntity user);

    void deleteAllByExpiredAtBefore(Instant expiredAt);

    Optional<SessionEntity> findByUserAndRole(UserEntity user, RoleEntity role);
}