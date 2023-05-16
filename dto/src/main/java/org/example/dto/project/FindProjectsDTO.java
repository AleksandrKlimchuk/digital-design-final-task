package org.example.dto.project;

import lombok.Value;
import org.example.status.ProjectStatus;

import java.util.List;


@Value
public class FindProjectsDTO {

    String filter;
    List<ProjectStatus> statuses;
}
