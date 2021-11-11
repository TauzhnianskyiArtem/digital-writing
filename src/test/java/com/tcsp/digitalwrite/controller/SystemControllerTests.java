package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.SystemController;
import com.tcsp.digitalwrite.api.dto.SystemDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
public class SystemControllerTests {

    String nameSystem = "Test system";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void addSystem() throws Exception {

        MvcResult result = mockMvc.perform(post(SystemController.CREATE_SYSTEM)
                .param("name", this.nameSystem
                ))
                .andExpect(status().isOk())
                .andReturn();
        SystemDto system = objectMapper.readValue(result.getResponse().getContentAsString(), SystemDto.class);
        assert(system.getName().equals(this.nameSystem));

    }

    @Test
    public void deleteSystem() throws Exception {

        SystemEntity system = systemRepository.findByName(this.nameSystem).get();

        mockMvc.perform(delete(SystemController.DELETE_SYSTEM.replace("{system_id}", system.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void emptyName() throws Exception {
       mockMvc.perform(post(SystemController.CREATE_SYSTEM)
                        .param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.SYSTEM_NAME_EMPTY, result.getResolvedException().getMessage()));
    }

    @Test
    public void notUniqName() throws Exception {
        mockMvc.perform(post(SystemController.CREATE_SYSTEM)
                        .param("name", "System 1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.EXIST_SYSTEM, result.getResolvedException().getMessage()));

    }

}
