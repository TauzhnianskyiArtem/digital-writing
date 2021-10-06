package com.tcsp.digitalwrite.api.controller.helper;

import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import com.tcsp.digitalwrite.store.repository.SessionRepository;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ControllerHelper {

    RoleRepository roleRepository;

    UserRepository userRepository;

    SystemRepository systemRepository;

    SessionRepository sessionRepository;



    public SystemEntity getSystemOrThrowException(String tokenSystem) {

        return systemRepository
                .findByToken(tokenSystem)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "System with \"%s\" doesn't exist.",
                                        tokenSystem
                                )
                        )
                );
    }

    public RoleEntity getRoleOrThrowException(String name){
        return roleRepository
                .findByName(name)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Role with \"%s\" doesn't exist.",
                                        name
                                )
                        )
                );
    }

    public UserEntity getUserOrThrowException(String token){
        return userRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist.",
                                        token
                                )
                        )
                );
    }

    public SessionEntity getSessionOrThrowException(String sessionId){
        return sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Session with \"%s\" doesn't exist.",
                                        sessionId
                                )
                        )
                );
    }

    public UserEntity getUserByParametersOrThrowException(
            Double typingSpeed,
            Double accuracy,
            Double holdTime,
            SystemEntity system
    ){
       return userRepository.findByTypingSpeedAndAccuracyAndHoldTimeAndSystem(
                typingSpeed,
                accuracy,
                holdTime,
                system
        ).orElseThrow(() -> new NotFoundException("User doesn't exist"));


    }
}
