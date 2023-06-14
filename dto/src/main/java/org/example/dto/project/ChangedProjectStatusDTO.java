package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

@Schema(description = "Информация об измененном статусе проекта")
@Value
public class ChangedProjectStatusDTO {

    @Schema(description = "Новый статус проекта")
    @NonNull
    ProjectStatus newStatus;
}
