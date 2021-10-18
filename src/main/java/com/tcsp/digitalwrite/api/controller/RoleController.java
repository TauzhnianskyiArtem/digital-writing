package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.RoleDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.SystemException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class RoleController {

    RoleRepository roleRepository;

    UserRepository userRepository;

    ControllerHelper controllerHelper;

    public static final String FETCH_ROLES = "/api/roles";
    public static final String CREATE_ROLES = "/api/roles";
    public static final String CHANGE_ROLES_USER = "/api/users/roles/change";
    public static final String FETCH_ROLES_USER = "/api/users/roles";

    @GetMapping(FETCH_ROLES)
    public List<String> fetchRoles(
            @RequestParam(value = "system_id") String systemId
    ) {
        controllerHelper.getSystemOrThrowException(systemId);

        List<RoleEntity> roles = roleRepository.findAll();

        List<String> result = roles.stream().map(role -> role.getName()).collect(Collectors.toList());

        return result;
    }

    @PostMapping(CREATE_ROLES)
    public AnswerDto createSystem(
            @RequestParam(value = "name") Optional<String> optionalName,
            @RequestParam(value = "token_system") String tokenSystem
    ){
        controllerHelper.getSystemOrThrowException(tokenSystem);

        optionalName = optionalName.filter(name -> !name.trim().isEmpty());

        RoleEntity role = optionalName
                .map((name) ->
                        RoleEntity.builder()
                                .name(name)
                                .build())
                .orElseThrow(() ->  new BadRequestException(Constants.ROLE_EMPTY));

        try {
            roleRepository.save(role);
            return AnswerDto.makeDefault(Constants.CREATE_ROLE);
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    @PutMapping(CHANGE_ROLES_USER)
    public RoleDto changeRoles(
            @RequestParam(value = "token_user") String tokenUser,
            @RequestParam(value = "roles") List<String> roles,
            @RequestParam(value = "system_id") String systemId
    ){
        controllerHelper.getSystemOrThrowException(systemId);

        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        user.setRoles(roles.stream()
                .map(role -> controllerHelper.getRoleOrThrowException(role))
                .collect(Collectors.toSet()));

        try {

            UserEntity savedUser = userRepository.saveAndFlush(user);
            return RoleDto.makeDefault(savedUser);

        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }

    }

    @GetMapping(FETCH_ROLES_USER)
    public List<String> fetchRolesByUser(
            @RequestParam(value = "token_user") String tokenUser,
            @RequestParam(value = "system_id") String systemId
    ){

        controllerHelper.getSystemOrThrowException(systemId);

        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        List<String> roles = user.getRoles().stream().map( r -> r.getName()).collect(Collectors.toList());

        return roles;

    }
}
