package org.example.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Schema(description = "Список сотрудников, удовлетворяющих фильтру")
public class FoundEmployeesDTO {

    @NonNull
    @Schema(description = "Сотрудники, удовлетворяющие фильтру")
    List<FoundEmployeeDTO> foundEmployees;
}
