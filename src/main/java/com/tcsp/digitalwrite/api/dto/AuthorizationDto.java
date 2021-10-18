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
    String name;

    @NonNull
    @JsonProperty("token_user")
    String tokenUser;

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

    public static AuthorizationDto makeDefault(SessionEntity entity){
        return AuthorizationDto.builder()
                .name(entity.getUser().getName())
                .tokenUser(entity.getUser().getToken())
                .sessionId(entity.getId())
                .createdAt(entity.getCreatedAt())
                .expiredAt(entity.getExpiredAt())
                .build();

    }
}
