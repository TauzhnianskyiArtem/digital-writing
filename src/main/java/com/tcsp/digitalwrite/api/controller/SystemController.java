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
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class SystemController {

    SystemRepository systemRepository;

    ControllerHelper controllerHelper;

    public static final String CREATE_SYSTEM = "/api/systems";
    public static final String DELETE_SYSTEM = "/api/systems/{system_id}";

    @PostMapping(CREATE_SYSTEM)
    @Transactional
    public SystemDto createSystem(
            @RequestParam(value = "name") Optional<String> optionalName
    ){

        optionalName = optionalName.filter(name -> !name.trim().isEmpty());
        String id = UUID.randomUUID().toString();

        if (optionalName.isPresent() && systemRepository.existsByName(optionalName.get())) {
            log.error(Constants.EXIST_SYSTEM + ": " + optionalName.get());
            throw new BadRequestException(Constants.EXIST_SYSTEM);
        }

        SystemEntity system = optionalName
                .map((name) ->
                        SystemEntity.builder()
                                .name(name)
                                .id(id)
                                .build())
                .orElseThrow(() ->  new BadRequestException(Constants.SYSTEM_NAME_EMPTY));
        log.debug("Name: " + optionalName.get());
        try {
            SystemEntity savedSystem = systemRepository.saveAndFlush(system);
            return SystemDto.makeDefault(savedSystem);
        } catch (PersistenceException e){
            log.error(Constants.ERROR_SERVICE);
            throw new SystemException(Constants.ERROR_SERVICE);
        }
    }

    @DeleteMapping(DELETE_SYSTEM)
    @Transactional
    public AnswerDto deleteSystem(@PathVariable("system_id") String systemId) {

        SystemEntity system = controllerHelper.getSystemOrThrowException(systemId);

        try {
            systemRepository.delete(system);
        } catch (PersistenceException e){
            throw new SystemException(Constants.ERROR_SERVICE);
        }

        return AnswerDto.makeDefault(Constants.DELETE_SYSTEM);
    }

}
