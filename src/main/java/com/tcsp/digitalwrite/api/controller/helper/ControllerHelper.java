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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ControllerHelper {

    RoleRepository roleRepository;

    UserRepository userRepository;

    SystemRepository systemRepository;

    SessionRepository sessionRepository;



    public SystemEntity getSystemOrThrowException(String systemId) {
        try {
            return systemRepository
                    .findById(systemId)
                    .orElseThrow(() -> {
                            log.error(Constants.NOT_EXIST_SYSTEM);
                            return new NotFoundException(Constants.NOT_EXIST_SYSTEM);
                        }
                    );
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public RoleEntity getRoleOrThrowException(String name){
        try {
            return roleRepository
                    .findByName(name)
                    .orElseThrow(() -> {
                            log.error(Constants.NOT_EXIST_ROLE);
                            return new NotFoundException(Constants.NOT_EXIST_ROLE);
                        }
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
                    .orElseThrow(() -> {
                            log.error(Constants.NOT_EXIST_SESSION);
                            return new NotFoundException(Constants.NOT_EXIST_SESSION);
                        }
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
                    system).orElseThrow(() -> {
                        log.error(Constants.NOT_EXIST_USER);
                        return new NotFoundException(Constants.NOT_EXIST_USER);
                    });
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    public void checkUserRoleOrThrowException(UserEntity user, RoleEntity role){
        user.getRoles().stream()
                .filter(r -> r.equals(role))
                .findFirst()
                .orElseThrow(() -> {
                        log.error(Constants.NOT_USER_ROLE);
                        return new NotFoundException(Constants.NOT_USER_ROLE);
                    }
                );
    }
}
