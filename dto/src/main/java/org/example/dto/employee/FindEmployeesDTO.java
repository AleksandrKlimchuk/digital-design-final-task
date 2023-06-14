package org.example.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Фильтр для поиска сотрудников")
public class FindEmployeesDTO {

    @Schema(description = "Текстовое значение фильтра", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    String filter;
}
