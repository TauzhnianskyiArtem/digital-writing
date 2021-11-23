package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.RoleController;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.RoleChangeDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.repository.RoleRepository;
import com.tcsp.digitalwrite.store.repository.SystemRepository;
import com.tcsp.digitalwrite.store.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    String nameRole = "TEST";

    String nameSystem = "System 1";

    String nameUser = "User 1";

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Transactional
    @Test
    public void addRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();
        MvcResult result = mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", this.nameRole)
                        .param("system_id", systemdId))
                .andExpect(status().isOk())
                .andReturn();

        AnswerDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), AnswerDto.class);
        assert(dto.getData().equals(Constants.CREATE_ROLE));

        roleRepository.deleteByName(this.nameRole);
    }

    @Test
    public void wrongSystemId() throws Exception {

        String systemdId = "wrong";

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", this.nameRole)
                        .param("system_id", systemdId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));;
    }

    @Test
    public void emptyRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.ROLE_EMPTY,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void notUpperCaseRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "test")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.ROLE_UPPER_CASE,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void notUniqRole() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RoleController.CREATE_ROLES)
                        .param("name", "USER")
                        .param("system_id", systemdId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.EXIST_ROLE,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void changeRole() throws Exception {

        String tokenUser = userRepository.findByName(this.nameUser).getToken();

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        String role = "USER";

        MvcResult result = mockMvc.perform(put(RoleController.CHANGE_ROLES_USER)
                        .param("system_id", systemdId)
                        .param("token_user", tokenUser)
                        .param("roles", role))
                .andExpect(status().isOk())
                .andReturn();
        RoleChangeDto dto = objectMapper.readValue(
                result.getResponse().getContentAsString(),  RoleChangeDto.class);
        assert(dto.getData().equals(Constants.CHANGE_ROLES));
    }

    @Test
    public void wrongSystemIdChangeRole() throws Exception {

        String tokenUser = userRepository.findByName(this.nameUser).getToken();

        String systemdId = "wrong";

        String role = "USER";

        mockMvc.perform(put(RoleController.CHANGE_ROLES_USER)
                        .param("system_id", systemdId)
                        .param("token_user", tokenUser)
                        .param("roles", role))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));;
    }

    @Test
    public void wrongTokenUserChangeRole() throws Exception {

        String tokenUser = "wrong";

        String systemdId = systemRepository.findByName(nameSystem).get().getId();

        String role = "USER";

        mockMvc.perform(put(RoleController.CHANGE_ROLES_USER)
                        .param("system_id", systemdId)
                        .param("token_user", tokenUser)
                        .param("roles", role))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_USER,
                        result.getResolvedException().getMessage()));;
    }

    @Test
    public void wrongRoleChangeRole() throws Exception {

        String tokenUser = userRepository.findByName(nameUser).getToken();

        String systemdId = systemRepository.findByName(nameSystem).get().getId();

        String role = "WRONG";

        mockMvc.perform(put(RoleController.CHANGE_ROLES_USER)
                        .param("system_id", systemdId)
                        .param("token_user", tokenUser)
                        .param("roles", role))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_ROLE,
                        result.getResolvedException().getMessage()));;
    }

}
