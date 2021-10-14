package com.tcsp.digitalwrite.api.controller;

import com.tcsp.digitalwrite.api.controller.helper.ControllerHelper;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.SystemDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.SystemException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class SystemController {

    SystemRepository systemRepository;

    ControllerHelper controllerHelper;

    public static final String CREATE_SYSTEM = "/api/systems";
    public static final String DELETE_SYSTEM = "/api/systems/{token_system}";

    @PostMapping(CREATE_SYSTEM)
    public SystemDto createSystem(
            @RequestParam(value = "name") Optional<String> optionalName
    ){
        optionalName = optionalName.filter(name -> !name.trim().isEmpty());
        String token = UUID.randomUUID().toString();

        SystemEntity system = optionalName
                .map((name) ->
                        SystemEntity.builder()
                                .name(name)
                                .token(token)
                                .build())
                .orElseThrow(() ->  new BadRequestException(Constants.SYSTEM_NAME_EMPTY));

        try {
            SystemEntity savedSystem = systemRepository.saveAndFlush(system);
            return SystemDto.makeDefault(savedSystem);
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    @DeleteMapping(DELETE_SYSTEM)
    public AnswerDto deleteSystem(@PathVariable("token_system") String tokenSystem) {

        SystemEntity system = controllerHelper.getSystemOrThrowException(tokenSystem);

        try {
            systemRepository.delete(system);
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }

        return AnswerDto.makeDefault(Constants.DELETE_SYSTEM);
    }

}
