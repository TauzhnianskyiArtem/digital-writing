package com.tcsp.digitalwrite.store.repository;

import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByToken(String token);

    UserEntity findByName(String name);

    Optional<UserEntity> findByTypingSpeedAndAccuracyAndHoldTimeAndSystem(
            Double typingSpeed, Double accuracy, Double holdTime, SystemEntity system);

}
