package org.example.dto.project;

import lombok.NonNull;
import lombok.Value;
import org.example.status.ProjectStatus;

import java.util.List;


@Value
public class FindProjectsDTO {

    @NonNull
    String text;
    List<ProjectStatus> statuses;
}
