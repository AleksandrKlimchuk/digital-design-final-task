package org.example.mapping;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Mappings {

    public final String EMPLOYEE_BASE_PATH = "/employees";
    public final String EMPLOYEE_ACCOUNT_PATH = "/account";

    public final String PROJECT_BASE_PATH = "/projects";
    public final String PROJECT_TEAM_BASE_PATH = "/project-teams";
    public final String TASK_BASE_PATH = "/tasks";

    public final String RESOURCE_IDENTIFIER = "/{id}";
}
