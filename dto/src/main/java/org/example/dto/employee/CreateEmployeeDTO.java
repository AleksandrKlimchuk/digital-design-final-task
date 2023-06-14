package org.example.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Schema(description = "Необходимые значения для создания нового профиля сотрудника")
@Value
@Builder
public class CreateEmployeeDTO {

    @NonNull
    @Schema(description = "Фамилия")
    String lastName;
    @NonNull
    @Schema(description = "Имя")
    String firstName;
    @Schema(description = "Отчество", nullable = true)
    String patronymic;
    @Schema(description = "Учетная запись, уникальное значение среди активных сотрудников", nullable = true)
    String account;
    @Schema(description = "Электронная почта", nullable = true)
    String email;
}
