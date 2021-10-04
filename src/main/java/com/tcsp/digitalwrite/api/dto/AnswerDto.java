package com.tcsp.digitalwrite.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerDto {

    @NonNull
    String data;

    public static AnswerDto makeDefault(String data){
        return AnswerDto.builder().data(data).build();
    }
}
