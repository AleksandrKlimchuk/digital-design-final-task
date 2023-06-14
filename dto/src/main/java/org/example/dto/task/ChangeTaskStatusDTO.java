package org.example.dto.task;

import lombok.NonNull;
import lombok.Value;


@Value
public class ChangeTaskStatusDTO {

    @NonNull
    Long id;
}
