package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.RoleController;
import com.tcsp.digitalwrite.api.controller.SystemController;
import com.tcsp.digitalwrite.api.dto.SystemDto;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
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

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class RoleControllerTests {

    String nameRole = "TEST";

    String nameSystem = "System 1";

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    public void addRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();
        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", this.nameRole)
                        .param("system_id", systemdId))
                .andExpect(status().isOk());

        roleRepository.deleteByName(this.nameRole);
    }

}
