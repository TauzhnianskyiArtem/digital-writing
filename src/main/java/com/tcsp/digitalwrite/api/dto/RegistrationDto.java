package com.tcsp.digitalwrite.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcsp.digitalwrite.store.entity.UserEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDto {
    @NonNull
    @JsonProperty("token_user")
    String tokenUser;

    @NonNull
    @JsonProperty("name")
    String nameUser;

    @NonNull
    @Builder.Default
    String data = "User registered successfully";

    @NonNull
    @JsonProperty("roles")
    List<String> roles;

    public static RegistrationDto makeDefault(UserEntity entity){

        return RegistrationDto.builder()
                .tokenUser(entity.getToken())
                .nameUser(entity.getName())
                .roles(entity.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                .build();
    }
}
