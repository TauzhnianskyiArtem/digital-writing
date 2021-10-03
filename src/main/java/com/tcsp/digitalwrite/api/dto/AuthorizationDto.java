package com.tcsp.digitalwrite.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizationDto {
    @NonNull
    @JsonProperty("token_user")
    String tokenUser;

    @NonNull
    @JsonProperty("session_id")
    String sessionId;

    @NonNull
    @Builder.Default
    String data = "User is successfully authorized";

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    public static AuthorizationDto makeDefault(SessionEntity entity){
        return AuthorizationDto.builder()
                .tokenUser(entity.getUser().getToken())
                .sessionId(entity.getId())
                .createdAt(entity.getCreatedAt())
                .build();

    }
}
