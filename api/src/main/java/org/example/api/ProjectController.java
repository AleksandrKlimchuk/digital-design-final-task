package org.example.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.api.utils.swagger.ProjectSwaggerDescriptions;
import org.example.dto.ErrorDTO;
import org.example.dto.project.*;
import org.example.mapping.Mappings;
import org.example.service.ProjectService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Mappings.PROJECT_BASE_PATH)
@Tag(name = "Проекты", description = "Эндпоинты для работы с проектами")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {

    ProjectService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Создать проект",
            description = ProjectSwaggerDescriptions.CREATE_PROJECT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = ProjectSwaggerDescriptions.CREATE_PROJECT_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = CreatedProjectDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "409", description = ProjectSwaggerDescriptions.CREATE_PROJECT_ALREADY_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public CreatedProjectDTO createProject(@NonNull @RequestBody CreateProjectDTO projectData) {
        return service.createProject(projectData);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновить проект",
            description = ProjectSwaggerDescriptions.UPDATE_PROJECT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = ProjectSwaggerDescriptions.UPDATE_PROJECT_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "404", description = ProjectSwaggerDescriptions.UPDATE_PROJECT_NOT_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = ProjectSwaggerDescriptions.UPDATE_PROJECT_CONFLICT,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public void updateProjectProfile(@NonNull @RequestBody UpdateProjectDTO projectData) {
        service.updateProject(projectData);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Поиск проектов",
            description = ProjectSwaggerDescriptions.GET_PROJECTS_BY_FILTER_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "200", description = ProjectSwaggerDescriptions.GET_PROJECTS_BY_FILTER_SUCCESS,
            content = @Content(
                    schema = @Schema(implementation = FoundProjectsDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE
            )
    )
    @SecurityRequirement(name = "basicAuth")
    public FoundProjectsDTO findProjectByFilter(@ParameterObject FindProjectsDTO filterData) {
        return service.findProjectByFilter(filterData);
    }

    @PatchMapping(value = Mappings.RESOURCE_IDENTIFIER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Перевод проекта в другое состояние",
            description = ProjectSwaggerDescriptions.CHANGE_STATUS_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = ProjectSwaggerDescriptions.CHANGE_STATUS_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = ChangedProjectStatusDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = ProjectSwaggerDescriptions.CHANGE_STATUS_NOT_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public ChangedProjectStatusDTO advanceProject(
            @PathVariable @Parameter(description = "Уникальный идентификатор проекта") Long id
    ) {
        return service.advanceProject(id);
    }
}
