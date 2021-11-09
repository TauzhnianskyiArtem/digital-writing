package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.AuthorizationController;
import com.tcsp.digitalwrite.api.controller.RegistrationController;
import com.tcsp.digitalwrite.api.controller.SystemController;
import com.tcsp.digitalwrite.api.dto.AuthorizationDto;
import com.tcsp.digitalwrite.api.dto.SystemDto;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    MockMvc mockMvc;

    //    Test User
    String nameUser = "User 1";
    String nameSystem = "System 1";
    Double typingSpeed = 80d;
    Double accuracy = 0.5d;
    Double holdTime = 0.4d;
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

    }

    @Test()
    public void deleteSession() throws Exception {

        UserEntity user = userRepository.findByName(this.nameUser);
        SessionEntity session = sessionRepository.findByUser(user);

        mockMvc.perform(delete(AuthorizationController.DELETE_SESSION.replace("{session_id}", session.getId()))
                        .param("system_id", user.getSystem().getId()))
                .andExpect(status().isOk());
    }
}
