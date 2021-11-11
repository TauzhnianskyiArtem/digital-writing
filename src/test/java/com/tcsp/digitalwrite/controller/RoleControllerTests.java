package com.tcsp.digitalwrite.controller;

import com.tcsp.digitalwrite.api.controller.RoleController;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    public void emptyRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.ROLE_EMPTY, result.getResolvedException().getMessage()));
    }

    @Test
    public void notUpperCaseRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "test")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.ROLE_UPPER_CASE, result.getResolvedException().getMessage()));
    }

    @Test
    public void notUniqRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "USER")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.EXIST_ROLE, result.getResolvedException().getMessage()));
    }

}
