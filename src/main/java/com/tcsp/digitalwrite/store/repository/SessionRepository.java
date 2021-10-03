package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {
}