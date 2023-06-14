package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Schema(description = "Ошибка")
@Value
public class ErrorDTO {

    @Schema(description = "Описание ошибки")
    String message;
}
