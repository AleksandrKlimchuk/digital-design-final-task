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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mapping.Mappings;
import org.example.api.utils.swagger.TaskSwaggerDescriptions;
import org.example.dto.ErrorDTO;
import org.example.dto.project.ChangedProjectStatusDTO;
import org.example.dto.task.*;
import org.example.service.TaskService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(Mappings.TASK_BASE_PATH)
@Tag(name = "Задания", description = "Эндпоинты для работы с заданиями")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {

    TaskService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Создать задачу",
            description = TaskSwaggerDescriptions.CREATE_TASK_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = TaskSwaggerDescriptions.CREATE_TASK_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = CreatedTaskDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400", description = TaskSwaggerDescriptions.CREATE_TASK_BAD_REQUEST,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = TaskSwaggerDescriptions.CREATE_TASK_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public CreatedTaskDTO createTask(Principal credentials, @RequestBody CreateTaskDTO taskData) {
        return service.createTask(credentials, taskData);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Изменение задачи",
            description = TaskSwaggerDescriptions.UPDATE_TASK_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = TaskSwaggerDescriptions.UPDATE_TASK_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "400", description = TaskSwaggerDescriptions.UPDATE_TASK_BAD_REQUEST,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = TaskSwaggerDescriptions.UPDATE_TASK_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public void updateTask(Principal credentials, @RequestBody UpdateTaskDTO taskData) {
        service.updateTask(credentials, taskData);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Поиск задач",
            description = TaskSwaggerDescriptions.GET_TASKS_BY_FILTER_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "200", description = TaskSwaggerDescriptions.GET_TASKS_BY_FILTER_SUCCESS,
            content = @Content(
                    schema = @Schema(implementation = FoundTasksDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE
            )
    )
    @SecurityRequirement(name = "basicAuth")
    public FoundTasksDTO findTasksByFilter(@ParameterObject FindTasksDTO filter) {
        return service.findTasksByFilter(filter);
    }

    @PatchMapping(value = Mappings.RESOURCE_IDENTIFIER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Перевод задачи в другое состояние",
            description = TaskSwaggerDescriptions.CHANGE_STATUS_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = TaskSwaggerDescriptions.CHANGE_STATUS_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = ChangedProjectStatusDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = TaskSwaggerDescriptions.CHANGE_STATUS_NOT_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public ChangedTaskStatusDTO advanceTask(
            @PathVariable @Parameter(description = "Уникальный идентификатор задачи") Long id
    ) {
        return service.advanceTask(new ChangeTaskStatusDTO(id));
    }
}
