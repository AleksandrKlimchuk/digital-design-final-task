package org.example.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.example.status.EmployeeStatus;

@Value
@Builder
@Schema(description = "Информация о сотруднике")
public class FoundEmployeeDTO {

    @NonNull
    @Schema(description = "Уникальный идентификатор сотрудника")
    Long id;
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
    @NonNull
    @Schema(description = "Статус сотрудника")
    EmployeeStatus status;
}
