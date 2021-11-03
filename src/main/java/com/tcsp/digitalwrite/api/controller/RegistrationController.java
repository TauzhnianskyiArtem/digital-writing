package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.RegistrationDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.SystemException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class RegistrationController {

    UserRepository userRepository;

    ControllerHelper controllerHelper;


    public static final String CREATE_USER = "/api/registration";
    public static final String DELETE_USER = "/api/registration/{token_user}";

    @PostMapping(CREATE_USER)
    public RegistrationDto registerUser(
            @RequestParam(value = "name") Optional<String> optionalName,
            @RequestParam(value = "typing_speed") Double typingSpeed,
            @RequestParam Double accuracy,
            @RequestParam(value = "hold_time") Double holdTime,
            @RequestParam(value = "system_id") String systemId,
            @RequestParam(value = "user_roles") List<String> userRoles
    ){
        log.debug("name: " + optionalName.get());
        log.debug("typingSpeed: " + typingSpeed);
        log.debug("accuracy: " + accuracy);
        log.debug("holdTime: " + holdTime);
        log.debug("systemId: " + systemId);
        log.debug("roles: " + userRoles);


        optionalName = optionalName.filter( name -> !name.trim().isEmpty());

        SystemEntity system = controllerHelper.getSystemOrThrowException(systemId);



        Set<RoleEntity> roles = userRoles.stream()
                .map(role -> controllerHelper.getRoleOrThrowException(role))
                .collect(Collectors.toSet());

        String token = UUID.randomUUID().toString();

        UserEntity user = optionalName.map(nameUser ->
            UserEntity.builder()
                    .token(token)
                    .typingSpeed(typingSpeed)
                    .name(nameUser)
                    .accuracy(accuracy)
                    .holdTime(holdTime)
                    .system(system)
                    .roles(roles)
                    .build()
        ).orElseThrow(() -> {
                log.error(Constants.USER_NAME_EMPTY);
                return new BadRequestException(Constants.USER_NAME_EMPTY);
            }
        );

        try {
            UserEntity savedUser = userRepository.saveAndFlush(user);
            return RegistrationDto.makeDefault(savedUser);
        } catch (PersistenceException e) {
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    @DeleteMapping(DELETE_USER)
    public AnswerDto deleteUser(
            @PathVariable(value = "token_user") String tokenUser,
            @RequestParam(value = "system_id") String systemId
    ){

        log.debug("tokenUser: " + tokenUser);
        log.debug("systemId: " + systemId);

        controllerHelper.getSystemOrThrowException(systemId);

        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        try {
            userRepository.delete(user);
        } catch (PersistenceException e) {
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }

        return AnswerDto.makeDefault(Constants.DELETE_USER);
    }
}
