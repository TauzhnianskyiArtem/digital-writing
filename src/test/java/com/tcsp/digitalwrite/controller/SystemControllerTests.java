package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.SystemController;
import com.tcsp.digitalwrite.api.dto.SystemDto;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class SystemControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    MockMvc mockMvc;


    @Test
    public void addSystem() throws Exception {
        String nameSystem = "Test system";

        MvcResult result = mockMvc.perform(post(SystemController.CREATE_SYSTEM)
                .param("name", nameSystem))
                .andExpect(status().isOk())
                .andReturn();
        SystemDto system = objectMapper.readValue(result.getResponse().getContentAsString(), SystemDto.class);
        assert(system.getName().equals(nameSystem));

    }

    @Test
    public void deleteSystem() throws Exception {
        List<SystemEntity> systems = systemRepository.findAll();

        mockMvc.perform(delete(SystemController.DELETE_SYSTEM.replace("{system_id}", systems.get(0).getId())))
                .andExpect(status().isOk());
    }

}
