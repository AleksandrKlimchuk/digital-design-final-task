package org.example.dto.project;

import lombok.*;

import java.util.List;

@Value
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class FoundProjectsDTO {

    @NonNull
    List<FoundProjectDTO> foundProjects;
}
