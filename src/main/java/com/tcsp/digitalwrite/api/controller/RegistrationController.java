package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.RegistrationDto;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class RegistrationController {

    UserRepository userRepository;

    ControllerHelper controllerHelper;


    public static final String CREATE_USER = "/api/systems/registration/users";
    public static final String DELETE_USER = "/api/systems/registration/users/{token_user}";

    @PostMapping(CREATE_USER)
    public RegistrationDto createUser(
            @RequestParam(value = "name") String nameUser,
            @RequestParam(value = "typing_speed") Double typingSpeed,
            @RequestParam Double accuracy,
            @RequestParam(value = "frequency_keystroke") Double frequencyKeystroke,
            @RequestParam(value = "token_system") String tokenSystem,
            @RequestParam(value = "user_roles") List<String> userRoles
    ){
        SystemEntity system = controllerHelper.getSystemOrThrowException(tokenSystem);

        Set<RoleEntity> roles = userRoles.stream()
                .map(role -> controllerHelper.getRoleOrThrowException(role))
                .collect(Collectors.toSet());

        String token = UUID.randomUUID().toString();

        UserEntity user = UserEntity
                .builder()
                .token(token)
                .typingSpeed(typingSpeed)
                .name(nameUser)
                .accuracy(accuracy)
                .frequencyKeystroke(frequencyKeystroke)
                .system(system)
                .roles(roles)
                .build();

        UserEntity savedUser = userRepository.saveAndFlush(user);

        return RegistrationDto.makeDefault(savedUser);
    }

    @DeleteMapping(DELETE_USER)
    public Map<String, String> deleteUser(
            @PathVariable(value = "token_user") String tokenUser
    ){
        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        userRepository.delete(user);

        String result = String.format("User with %s deleted successfully", tokenUser);

        Map<String, String> data = new HashMap<>();

        data.put("data", result);

        return data;
    }
}
