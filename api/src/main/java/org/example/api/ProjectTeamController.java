package org.example.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.api.utils.swagger.ProjectTeamSwaggerDescription;
import org.example.mapping.Mappings;
import org.example.dto.ErrorDTO;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.ExcludeEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.service.ProjectTeamService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Mappings.PROJECT_TEAM_BASE_PATH)
@Tag(name = "Команды проекта", description = "Эндпоинты для работы с командами проектов")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectTeamController {

    ProjectTeamService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Добавить сотрудника",
            description = ProjectTeamSwaggerDescription.ADD_EMPLOYEE_IN_PROJECT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = ProjectTeamSwaggerDescription.ADD_EMPLOYEE_IN_PROJECT_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = ProjectTeamSwaggerDescription.ADD_EMPLOYEE_IN_PROJECT_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = ProjectTeamSwaggerDescription.ADD_EMPLOYEE_IN_PROJECT_ALREADY_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public void addEmployeeToProject(@RequestBody AddEmployeeDTO employeeAndProjectData) {
        service.addEmployeeToProject(employeeAndProjectData);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Исключить сотрудника",
            description = ProjectTeamSwaggerDescription.EXCLUDE_EMPLOYEE_FROM_PROJECT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = ProjectTeamSwaggerDescription.EXCLUDE_EMPLOYEE_FROM_PROJECT_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = ProjectTeamSwaggerDescription.EXCLUDE_EMPLOYEE_FROM_PROJECT_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public void excludeEmployeeFromProject(@RequestBody ExcludeEmployeeDTO employeeAndProjectData) {
        service.excludeEmployeeFromProject(employeeAndProjectData);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Получить всех участников проекта",
            description = ProjectTeamSwaggerDescription.GET_EMPLOYEES_FROM_PROJECT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = ProjectTeamSwaggerDescription.GET_EMPLOYEES_FROM_PROJECT_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = FoundAllEmployeesDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = ProjectTeamSwaggerDescription.GET_EMPLOYEES_FROM_PROJECT_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public FoundAllEmployeesDTO getAllEmployeesOfProject(
            @ParameterObject FindAllEmployeesDTO projectData
    ) {
        return service.getAllEmployeesOfProject(projectData);
    }
}
