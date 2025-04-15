package com.emobile.springtodo.exception.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

    @Schema(description = "HTTP статус код ошибки", example = "404")
    private int status;

    @Schema(description = "Сообщение об ошибке", example = "Задача не найдена")
    private String message;

    @Schema(description = "Время возникновения ошибки", example = "2025-03-22T15:24:31.966402100Z")
    private Instant timestamp;
}
