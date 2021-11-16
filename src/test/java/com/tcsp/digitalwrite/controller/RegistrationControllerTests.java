package com.tcsp.digitalwrite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsp.digitalwrite.api.controller.RegistrationController;
import com.tcsp.digitalwrite.api.dto.AnswerDto;
import com.tcsp.digitalwrite.api.dto.RegistrationDto;
import com.tcsp.digitalwrite.api.exception.BadRequestException;
import com.tcsp.digitalwrite.api.exception.NotFoundException;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.UserEntity;
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
public class RegistrationControllerTests {
    @Autowired
    SystemRepository systemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    //    Test User
    String nameSystem = "System 1";
    String name = "Test User";
    Double typingSpeed = 230d;
    Double accuracy = 90d;
    Double holdTime = 1d;

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
        RegistrationDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationDto .class);
        assert(dto.getData().equals(Constants.SUCCESS_REGISTERED));
    }

    @Test
    public void deleteUser() throws Exception {
        UserEntity user = userRepository.findByName(this.name);

        MvcResult result = mockMvc.perform(delete(
                RegistrationController.DELETE_USER.replace("{token_user}", user.getToken()))
                        .param("system_id", user.getSystem().getId()))
                .andExpect(status().isOk())
                .andReturn();
        AnswerDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), AnswerDto.class);
        assert(dto.getData().equals(Constants.DELETE_USER));

    }

    @Test
    public void emptyName() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", "")
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.USER_NAME_EMPTY,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void nagativeTypingSpeed() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", systemdId)
                        .param("typing_speed", "-10")
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.NEGATIVE_TYPING_SPEED,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void nagativeAccuracy() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", "-10")
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.WRONG_ACCURACY, result.getResolvedException().getMessage()));
    }

    @Test
    public void nagativeHoldTime() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", "-1")
                        .param("user_roles", "USER"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(Constants.NEGATIVE_HOLD_TIME, result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongSystemId() throws Exception {
        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", "wrong")
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongRole() throws Exception {

        String systemdId = systemRepository.findByName(this.nameSystem).get().getId();

        mockMvc.perform(post(RegistrationController.CREATE_USER)
                        .param("name", this.name)
                        .param("system_id", systemdId)
                        .param("typing_speed", this.typingSpeed.toString())
                        .param("accuracy", this.accuracy.toString())
                        .param("hold_time", this.holdTime.toString())
                        .param("user_roles", "USER","WRONG"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_ROLE,
                        result.getResolvedException().getMessage()));
    }

    @Test
    public void wrongSystemIdDeleteUser() throws Exception {
        String nameUser = "User 1";
        UserEntity user = userRepository.findByName(nameUser);

        mockMvc.perform(delete(RegistrationController.DELETE_USER.replace("{token_user}", user.getToken()))
                        .param("system_id", "wrong"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_SYSTEM,
                        result.getResolvedException().getMessage()));;

    }

    @Test
    public void wrongTokenUserDeleteUser() throws Exception {
        String nameUser = "User 1";
        UserEntity user = userRepository.findByName(nameUser);

        mockMvc.perform(delete(RegistrationController.DELETE_USER.replace("{token_user}", "wrong"))
                        .param("system_id", user.getSystem().getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(Constants.NOT_EXIST_USER,
                        result.getResolvedException().getMessage()));

    }

}
