package org.example.dto.employee;

import lombok.NonNull;
import lombok.Value;

@Value
public class DeletedEmployeeDTO {

    @NonNull
    Boolean wasDeleted;
}
