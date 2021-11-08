package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.AuthorizationController;
import com.tcsp.digitalwrite.api.controller.RegistrationController;
import com.tcsp.digitalwrite.api.controller.SystemController;
import com.tcsp.digitalwrite.api.dto.AuthorizationDto;
import com.tcsp.digitalwrite.api.dto.RegistrationDto;
import com.tcsp.digitalwrite.store.entity.UserEntity;
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
import org.springframework.util.MultiValueMapAdapter;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTests {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    //    Test User
    String nameSystem = "System 1";
    String name = "Test User";
    Double typingSpeed = 80d;
    Double accuracy = 0.5d;
    Double holdTime = 0.4d;

    @Test
    public void registerUser() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        MvcResult result = mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER"))
                .andExpect(status().isOk())
                .andReturn();
        RegistrationDto user = objectMapper
                .readValue(result.getResponse().getContentAsString(), RegistrationDto.class);
    }

    @Test
    public void deleteUser() throws Exception {
        UserEntity user = userRepository.findByName(this.name);

        mockMvc.perform(delete(RegistrationController.DELETE_USER.replace("{token_user}", user.getToken()))
                        .param("system_id", user.getSystem().getId()))
                        .andExpect(status().isOk());

    }
}
