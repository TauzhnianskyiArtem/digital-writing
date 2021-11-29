package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.RoleChangeDto;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
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
    @Transactional
    @Cacheable(value = "roleCache")
    public List<String> fetchRoles(
            @RequestParam(value = "system_id") String systemId
    ) {
        log.debug("systemId: " + systemId);

        controllerHelper.getSystemOrThrowException(systemId);

        List<RoleEntity> roles = roleRepository.findAll();

        List<String> result = roles.stream().map(role -> role.getName()).collect(Collectors.toList());

        return result;
    }

    @PostMapping(CREATE_ROLES)
    @Transactional
    @CacheEvict(value = "roleCache", allEntries = true)
    public AnswerDto addRole(
            @RequestParam(value = "name") Optional<String> optionalName,
            @RequestParam(value = "system_id") String systemId
    ){
        log.debug("optionalName: " + optionalName.get());
        log.debug("systemId: " + systemId);

        controllerHelper.getSystemOrThrowException(systemId);

        optionalName = optionalName.filter(name -> !name.trim().isEmpty());


        if (optionalName.isPresent() && roleRepository.existsByName(optionalName.get()))
            throw new BadRequestException(Constants.EXIST_ROLE);

        if (optionalName.isPresent() && !optionalName.get().toUpperCase().equals(optionalName.get()))
            throw new BadRequestException(Constants.ROLE_UPPER_CASE);


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
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    @PutMapping(CHANGE_ROLES_USER)
    @Transactional
    public RoleChangeDto changeRoles(
            @RequestParam(value = "token_user") String tokenUser,
            @RequestParam(value = "roles") List<String> roles,
            @RequestParam(value = "system_id") String systemId
    ){
        log.debug("tokenUser: " + tokenUser);
        log.debug("systemId: " + systemId);
        log.debug("roles: " + roles);


        controllerHelper.getSystemOrThrowException(systemId);

        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        user.setRoles(roles.stream()
                .map(role -> controllerHelper.getRoleOrThrowException(role))
                .collect(Collectors.toSet()));

        try {

            UserEntity savedUser = userRepository.saveAndFlush(user);
            return RoleChangeDto.makeDefault(savedUser);

        } catch (PersistenceException e){
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }

    }

    @GetMapping(FETCH_ROLES_USER)
    @Transactional
    public List<String> fetchRolesByUser(
            @RequestParam(value = "token_user") String tokenUser,
            @RequestParam(value = "system_id") String systemId
    ){

        log.debug("tokenUser: " + tokenUser);
        log.debug("systemId: " + systemId);

        controllerHelper.getSystemOrThrowException(systemId);

        UserEntity user = controllerHelper.getUserOrThrowException(tokenUser);

        List<String> roles = user.getRoles().stream().map( r -> r.getName()).collect(Collectors.toList());

        return roles;

    }
}
