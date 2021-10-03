package com.tcsp.digitalwrite.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcsp.digitalwrite.store.entity.SystemEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemDto {
    @NonNull
    @JsonProperty("token_system")
    String tokenSystem;

    @NonNull
    String name;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    public static SystemDto makeDefault(SystemEntity entity){
        return SystemDto.builder()
                .tokenSystem(entity.getToken())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();

    }
}
