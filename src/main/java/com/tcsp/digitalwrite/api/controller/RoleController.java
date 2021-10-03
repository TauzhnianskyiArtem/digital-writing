package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.dto.AckDto;
import com.tcsp.digitalwrite.api.dto.SystemDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.store.entity.RoleEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class RoleController {

    RoleRepository roleRepository;

    public static final String FETCH_ROLES = "/api/roles";
    public static final String CREATE_ROLES = "/api/roles";

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
        System.out.println();
        roleRepository.save(role);

        String result = String.format("Role with  %s created", role.getName());

        Map<String, String> data = new HashMap<>();

        data.put("data", result);

        return data;
    }
}
