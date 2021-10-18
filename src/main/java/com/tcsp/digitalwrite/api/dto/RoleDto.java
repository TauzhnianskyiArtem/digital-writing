package com.tcsp.digitalwrite.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcsp.digitalwrite.shared.Constants;
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
public class RoleDto {

    @NonNull
    @JsonProperty("token_user")
    String tokenUser;

    @NonNull
    @Builder.Default
    String data = Constants.CHANGE_ROLES;

    @NonNull
    @JsonProperty("roles")
    List<String> roles;

    public static RoleDto makeDefault(UserEntity user){
        return RoleDto.builder()
                .tokenUser(user.getToken())
                .roles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                .build();
    }
}
