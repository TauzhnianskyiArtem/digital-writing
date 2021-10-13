package com.tcsp.digitalwrite.api.controller.helper;

import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.shared.Constants;
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
                        new NotFoundException(Constants.NOT_EXIST_SYSTEM)
                );
    }

    public RoleEntity getRoleOrThrowException(String name){
        return roleRepository
                .findByName(name)
                .orElseThrow(() ->
                        new NotFoundException(Constants.NOT_EXIST_ROLE)
                );
    }

    public UserEntity getUserOrThrowException(String tokenUser){
        return userRepository
                .findByToken(tokenUser)
                .orElseThrow(() ->
                        new NotFoundException(Constants.NOT_EXIST_USER)
                );
    }

    public SessionEntity getSessionOrThrowException(String sessionId){
        return sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new NotFoundException(Constants.NOT_EXIST_SESSION)
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
        ).orElseThrow(() -> new NotFoundException(Constants.NOT_EXIST_USER));


    }
}
