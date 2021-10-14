package com.tcsp.digitalwrite.api.controller.helper;

import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.api.exception.SystemException;
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

import javax.persistence.PersistenceException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ControllerHelper {

    RoleRepository roleRepository;

    UserRepository userRepository;

    SystemRepository systemRepository;

    SessionRepository sessionRepository;



    public SystemEntity getSystemOrThrowException(String tokenSystem) {
        try {
            return systemRepository
                    .findByToken(tokenSystem)
                    .orElseThrow(() ->
                            new NotFoundException(Constants.NOT_EXIST_SYSTEM)
                    );
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public RoleEntity getRoleOrThrowException(String name){
        try {
            return roleRepository
                    .findByName(name)
                    .orElseThrow(() ->
                            new NotFoundException(Constants.NOT_EXIST_ROLE)
                    );

        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public UserEntity getUserOrThrowException(String tokenUser){
        try {
            return userRepository
                    .findByToken(tokenUser)
                    .orElseThrow(() ->
                            new NotFoundException(Constants.NOT_EXIST_USER)
                    );
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public SessionEntity getSessionOrThrowException(String sessionId){
        try {
            return sessionRepository
                    .findById(sessionId)
                    .orElseThrow(() ->
                            new NotFoundException(Constants.NOT_EXIST_SESSION)
                    );
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public UserEntity getUserByParametersOrThrowException(
            Double typingSpeed,
            Double accuracy,
            Double holdTime,
            SystemEntity system
    ){
        try {
            return userRepository.findByTypingSpeedAndAccuracyAndHoldTimeAndSystem(
                    typingSpeed,
                    accuracy,
                    holdTime,
                    system).orElseThrow(() -> new NotFoundException(Constants.NOT_EXIST_USER));
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }
}
