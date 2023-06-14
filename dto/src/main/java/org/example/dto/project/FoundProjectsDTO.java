package org.example.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "Список проектов, удовлетворяющих фильтру")
@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class FoundProjectsDTO {

    @Schema(description = "Проекты, удовлетворяющие фильтру")
    @NonNull
    List<FoundProjectDTO> foundProjects;
}
