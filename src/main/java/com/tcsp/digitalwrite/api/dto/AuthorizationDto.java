package com.tcsp.digitalwrite.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcsp.digitalwrite.shared.Constants;
import com.tcsp.digitalwrite.store.entity.SessionEntity;
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
    String nameUser;

    @NonNull
    @JsonProperty("session_id")
    String sessionId;

    @NonNull
    @Builder.Default
    String data = Constants.SUCCESS_AUTHORIZED;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    @NonNull
    @JsonProperty("expired_at")
    Instant expiredAt;

    @NonNull
    String role;

    public static AuthorizationDto makeDefault(SessionEntity entity){
        return AuthorizationDto.builder()
                .nameUser(entity.getUser().getName())
                .sessionId(entity.getId())
                .createdAt(entity.getCreatedAt())
                .expiredAt(entity.getExpiredAt())
                .role(entity.getRole().getName())
                .build();

    }
}
