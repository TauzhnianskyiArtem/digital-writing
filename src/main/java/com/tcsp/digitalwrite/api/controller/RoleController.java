package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.RoleDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static final String CHANGE_ROLES_USER = "/api/roles/change";

    @GetMapping(FETCH_ROLES)
    public List<String> fetchRoles() {

        List<RoleEntity> roles = roleRepository.findAll();

        List<String> result = roles.stream().map(role -> role.getName()).collect(Collectors.toList());

        return result;
    }

    @PostMapping(CREATE_ROLES)
    public Map<String, String> createSystem(
            @RequestParam(value = "name") Optional<String> optionalName
    ){
        optionalName = optionalName.filter(name -> !name.trim().isEmpty());

        RoleEntity role = optionalName
                .map((name) ->
                        RoleEntity.builder()
                                .name(name)
                                .build())
                .orElseThrow(() ->  new BadRequestException("Role name can't be empty."));

        roleRepository.save(role);

        String result = String.format("Role with  %s created", role.getName());

        Map<String, String> data = new HashMap<>();

        data.put("data", result);

        return data;
    }

    @PutMapping(CHANGE_ROLES_USER)
    public RoleDto changeRoles(
            @RequestParam(value = "token_user") String tokenUser,
            @RequestParam(value = "roles") List<String> roles
    ){
        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        user.setRoles(roles.stream()
                .map(role -> controllerHelper.getRoleOrThrowException(role))
                .collect(Collectors.toSet()));

        UserEntity savedUser = userRepository.saveAndFlush(user);

        return RoleDto.makeDefault(savedUser);
    }
}
