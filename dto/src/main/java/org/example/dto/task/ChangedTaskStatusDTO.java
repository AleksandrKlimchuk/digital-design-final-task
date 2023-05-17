package org.example.dto.task;

import lombok.NonNull;
import lombok.Value;
import org.example.status.TaskStatus;

@Value
public class ChangedTaskStatusDTO {

    @NonNull
    TaskStatus newTaskStatus;
}
