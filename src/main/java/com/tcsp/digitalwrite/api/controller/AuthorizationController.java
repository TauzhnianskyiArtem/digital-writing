package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.AuthorizationDto;
import com.tcsp.digitalwrite.api.exception.SystemException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.SessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.UUID;

@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class AuthorizationController {

    SessionRepository sessionRepository;

    ControllerHelper controllerHelper;

    public static final String CREATE_SESSION = "/api/users/auth";
    public static final String DELETE_SESSION = "/api/users/auth/{session_id}";

    @PostMapping(CREATE_SESSION)
    public AuthorizationDto authorizeUser(
            @RequestParam(value = "typing_speed") Double typingSpeed,
            @RequestParam Double accuracy,
            @RequestParam(value = "hold_time") Double holdTime,
            @RequestParam(value = "role") String roleName,
            @RequestParam(value = "system_id") String systemId

    ) {
        log.debug("typingSpeed: " + typingSpeed);
        log.debug("accuracy: " + accuracy);
        log.debug("holdTime: " + holdTime);
        log.debug("roleName: " + roleName);
        log.debug("systemId: " + systemId);

        SystemEntity system = controllerHelper.getSystemOrThrowException(systemId);

        RoleEntity role = controllerHelper.getRoleOrThrowException(roleName);


        UserEntity user = controllerHelper.getUserByParametersOrThrowException(
                typingSpeed,
                accuracy,
                holdTime,
                system);

        controllerHelper.checkUserRoleOrThrowException(user, role);

        String id = UUID.randomUUID().toString();

        SessionEntity session = SessionEntity.builder()
                .id(id)
                .user(user)
                .role(role)
                .build();
        try {

            SessionEntity savedSession = sessionRepository.saveAndFlush(session);
            return AuthorizationDto.makeDefault(savedSession);

        } catch (PersistenceException e) {
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }

    }

    @DeleteMapping(DELETE_SESSION)
    public AnswerDto logOutUser(
            @PathVariable("session_id") String sessionId,
            @RequestParam(value = "system_id") String systemId
    ){
        controllerHelper.getSystemOrThrowException(systemId);

        SessionEntity session = controllerHelper.getSessionOrThrowException(sessionId);

        try {
            sessionRepository.delete(session);
        } catch (PersistenceException e) {
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }

        return AnswerDto.makeDefault(Constants.DELETE_SESSION);

    }

}
