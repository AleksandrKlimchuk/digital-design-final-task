package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.status.ProjectStatus;

import java.util.List;


@Schema(description = "Фильтр для поиска проектов")
@Value
public class FindProjectsDTO {

    @Schema(description = "Текстовое значение фильтра", nullable = true)
    String text;
    List<ProjectStatus> statuses;
}
