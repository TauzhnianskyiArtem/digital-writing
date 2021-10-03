package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AuthorizationDto;
import com.tcsp.digitalwrite.api.dto.RegistrationDto;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.SessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class AuthorizationController {

    SessionRepository sessionRepository;

    ControllerHelper controllerHelper;

    public static final String CREATE_SESSION = "/api/users/sessions";
    public static final String DELETE_SESSION = "/api/users/sessions/{session_id}";

    @PostMapping(CREATE_SESSION)
    public AuthorizationDto createSession(
            @RequestParam(value = "typing_speed") Double typingSpeed,
            @RequestParam Double accuracy,
            @RequestParam(value = "frequency_keystroke") Double frequencyKeystroke,
            @RequestParam(value = "token_system") String tokenSystem
    ) {
        SystemEntity system = controllerHelper.getSystemOrThrowException(tokenSystem);

        UserEntity user = controllerHelper.getUserByParametersOrThrowException(
                typingSpeed,
                accuracy,
                frequencyKeystroke,
                system);

        String id = UUID.randomUUID().toString();

        SessionEntity session = SessionEntity.builder()
                .id(id)
                .user(user)
                .build();

        SessionEntity savedSession = sessionRepository.saveAndFlush(session);

        return AuthorizationDto.makeDefault(savedSession);
    }

    @DeleteMapping(DELETE_SESSION)
    public Map<String, String> deleteSession(
            @PathVariable("session_id") String sessionId
    ){
        SessionEntity session = controllerHelper.getSessionOrThrowException(sessionId);

        sessionRepository.delete(session);

        String result = "User successfully logged out";

        Map<String, String> data = new HashMap<>();

        data.put("data", result);

        return data;

    }

}
