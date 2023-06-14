package org.example.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

@Value
@Schema(description = "Созданный профиль сотрудника")
public class CreatedEmployeeDTO {

    @NonNull
    @Schema(description = "Уникальный идентификатор созданного профиля сотрудника")
    Long id;
}
