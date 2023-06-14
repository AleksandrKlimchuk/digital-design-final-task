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
import org.example.api.utils.swagger.EmployeeSwaggerDescriptions;
import org.example.dto.ErrorDTO;
import org.example.dto.employee.*;
import org.example.mapping.Mappings;
import org.example.service.EmployeeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(Mappings.EMPLOYEE_BASE_PATH)
@Tag(name = "Сотрудники", description = "Эндпоинты для работы с сотрудниками")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {

    EmployeeService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Создать профиль сотрудника",
            description = EmployeeSwaggerDescriptions.CREATE_EMPLOYEE_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = EmployeeSwaggerDescriptions.CREATE_EMPLOYEE_SUCCESS_RESPONSE,
                    content = @Content(
                            schema = @Schema(implementation = CreatedEmployeeDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "409", description = EmployeeSwaggerDescriptions.CREATE_EMPLOYEE_ALREADY_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    public ResponseEntity<CreatedEmployeeDTO> createEmployeeProfile(
            @NonNull @RequestBody CreateEmployeeDTO employeeData
    ) {
        final CreatedEmployeeDTO createdEmployeeProfile = service.createEmployeeProfile(employeeData);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path(Mappings.RESOURCE_IDENTIFIER)
                                .buildAndExpand(createdEmployeeProfile.getId())
                                .toUri()
                )
                .body(createdEmployeeProfile);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Обновить профиль сотрудника",
            description = EmployeeSwaggerDescriptions.UPDATE_EMPLOYEE_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = EmployeeSwaggerDescriptions.UPDATE_EMPLOYEE_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "404", description = EmployeeSwaggerDescriptions.UPDATE_EMPLOYEE_NOT_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = EmployeeSwaggerDescriptions.UPDATE_EMPLOYEE_CONFLICT,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    public void updateEmployeeProfile(@NonNull @RequestBody UpdateEmployeeDTO employeeData) {
        service.updateEmployeeProfile(employeeData);
    }

    @DeleteMapping(Mappings.RESOURCE_IDENTIFIER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить профиль сотрудника",
            description = EmployeeSwaggerDescriptions.DELETE_EMPLOYEE_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = EmployeeSwaggerDescriptions.DELETE_EMPLOYEE_SUCCESS_RESPONSE
            ),
            @ApiResponse(
                    responseCode = "404", description = EmployeeSwaggerDescriptions.DELETE_EMPLOYEE_NOT_EXISTS,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    public void deleteEmployeeProfile(
            @NonNull @PathVariable @Parameter(description = "Уникальный идентификатор") Long id
    ) {
        service.deleteEmployeeProfile(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Получить сотрудников",
            description = EmployeeSwaggerDescriptions.GET_EMPLOYEES_BY_FILTER_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "200", description = EmployeeSwaggerDescriptions.GET_EMPLOYEES_BY_FILTER_SUCCESS,
            content = @Content(
                    schema = @Schema(implementation = FoundEmployeesDTO.class),
                    mediaType = MediaType.APPLICATION_JSON_VALUE
            )
    )
    @SecurityRequirement(name = "basicAuth")
    public FoundEmployeesDTO findEmployeesByFilter(@ParameterObject FindEmployeesDTO filter) {
        return service.findEmployeesByFilter(filter);
    }

    @GetMapping(value = Mappings.RESOURCE_IDENTIFIER, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Получить сотрудника по уникальному идентификатору",
            description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_ID_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_IDENTIFIER_SUCCESS,
                    content = @Content(
                            schema = @Schema(implementation = FoundEmployeeDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_ID_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public FoundEmployeeDTO findEmployeeById(
            @PathVariable @Parameter(description = "Уникальный идентификатор") Long id
    ) {
        return service.findEmployeeById(id);
    }

    @GetMapping(value = Mappings.EMPLOYEE_ACCOUNT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Получить сотрудника по учетной записи",
            description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_ACCOUNT_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_IDENTIFIER_SUCCESS,
                    content = @Content(
                            schema = @Schema(implementation = FoundEmployeeDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = EmployeeSwaggerDescriptions.GET_EMPLOYEE_BY_ACCOUNT_NOT_FOUND,
                    content = @Content(
                            schema = @Schema(implementation = ErrorDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
            )
    })
    @SecurityRequirement(name = "basicAuth")
    public FoundEmployeeDTO findEmployeeByAccount(
            @RequestParam @Parameter(description = "Учетная запись") String account
    ) {
        return service.findEmployeeByAccount(account);
    }
}
