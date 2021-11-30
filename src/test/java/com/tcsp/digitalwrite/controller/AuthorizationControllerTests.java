package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.AuthorizationController;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.AuthorizationDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import com.tcsp.digitalwrite.store.repository.SessionRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationControllerTests {

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    //    Test User
    String nameUser = "User 1";
    String nameSystem = "System 1";
    Double typingSpeed = 50d;
    Double accuracy = 100d;
    Double holdTime = 0.8d;
    String roleName = "USER";

    @Test
    public void authorizeUser() throws Exception {
        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        MvcResult result = mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                .param("system_id", systemdId)
                .param("typing_speed", this.typingSpeed.toString())
                .param("accuracy", this.accuracy.toString())
                .param("hold_time", this.holdTime.toString())
                .param("role", this.roleName))
                .andExpect(status().isOk())
                .andReturn();

        AuthorizationDto dto = objectMapper.readValue(
                result.getResponse().getContentAsString(),  AuthorizationDto.class);
        assert(dto.getData().equals(Constants.SUCCESS_AUTHORIZED));
    }

    @Test()
    public void deleteSession() throws Exception {

        UserEntity user = userRepository.findByName(this.nameUser);
        SessionEntity session = sessionRepository.findByUser(user);

        MvcResult result = mockMvc.perform(delete(AuthorizationController.DELETE_SESSION.replace("{session_id}", session.getId()))
                        .param("system_id", user.getSystem().getId()))
                .andExpect(status().isOk())
                .andReturn();
        AnswerDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), AnswerDto.class);
        assert(dto.getData().equals(Constants.DELETE_SESSION));
    }

    @Test
    public void nagativeTypingSpeed() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", "-10")
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("role", this.roleName))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.NEGATIVE_TYPING_SPEED,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void nagativeAccuracy() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", "-10")
                        .param("hold_time", this.holdTime.toString())
                        .param("role", this.roleName))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.WRONG_ACCURACY,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void nagativeHoldTime() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", "-1")
                        .param("role", this.roleName))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.NEGATIVE_HOLD_TIME,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongSystemId() throws Exception {
        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", "wrong")
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("role", this.roleName))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongRole() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("role", "WRONG"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_ROLE,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void notHaveUserRole() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("role", "MODERATOR"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_USER_ROLE, result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongUser() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(AuthorizationController.CREATE_SESSION)
                        .param("system_id", systemdId)
                        .param("typing_speed", "1")
                        .param("accuracy", "1")
                        .param("hold_time", "1")
                        .param("role", "ADMIN"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_USER, result.getResolvedException().getMessage()));
    }

    @Test()
    public void wrongSessionId() throws Exception {

        UserEntity user = userRepository.findByName(this.nameUser);
        String sessionId = "wrong";

        mockMvc.perform(delete(AuthorizationController.DELETE_SESSION.replace("{session_id}", sessionId))
                        .param("system_id", user.getSystem().getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SESSION,
                        result.getResolvedException().getMessage()));
    }

    @Test()
    public void wrongSystemIdDeleteSession() throws Exception {

        UserEntity user = userRepository.findByName(this.nameUser);
        SessionEntity session = sessionRepository.findByUser(user);

        mockMvc.perform(delete(AuthorizationController.DELETE_SESSION.replace("{session_id}", session.getId()))
                        .param("system_id", "wrong"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));
    }

}
