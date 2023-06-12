package org.example.service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.task.*;
import org.example.exception.AuthorNotExistsException;
import org.example.exception.EarlyDeadlineException;
import org.example.exception.EntityNotExistsException;
import org.example.mapper.task.CreateTaskMapper;
import org.example.mapper.task.FindTasksMapper;
import org.example.service.specification.FindTaskSpecification;
import org.example.service.utils.ServiceUtils;
import org.example.status.TaskStatus;
import org.example.store.model.Employee;
import org.example.store.model.ProjectTeam;
import org.example.store.model.Task;
import org.example.store.repository.TaskRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository repository;

    EmployeeService employeeService;
    ProjectTeamService projectTeamService;

    CreateTaskMapper createTaskMapper;
    FindTasksMapper findTasksMapper;

    ObjectProvider<FindTaskSpecification> findTaskSpecificationProvider;

    public CreatedTaskDTO createTask(@NonNull Principal credentials, @NonNull CreateTaskDTO taskData) {
        final Employee authorProfile = employeeService.getEmployeeByAccount(credentials.getName());
        final ProjectTeam authorEmployeeInProject = fetchAuthorOfProject(
                authorProfile.getId(), taskData.getProjectId()
        );
        final Employee executorEmployee = fetchEmployeeWhichActiveMemberOfProject(
                taskData.getExecutorId(), taskData.getProjectId()
        );
        final Task task = createTaskMapper.mapToEntity(taskData);
        task.setProject(authorEmployeeInProject.getProject());
        task.setExecutor(executorEmployee);
        task.setAuthor(authorEmployeeInProject.getEmployee());
        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        checkDeadline(task);
        final Task savedTask = repository.save(task);
        return createTaskMapper.mapToResult(savedTask);
    }

    public void updateTask(@NonNull Principal credentials, @NonNull UpdateTaskDTO taskData) {
        final Employee authorProfile = employeeService.getEmployeeByAccount(credentials.getName());
        final Task storedTask = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, taskData::getId, () -> new EntityNotExistsException(
                        "Task with id %d doesn't exist and so can't be updated".formatted(taskData.getId())
                )
        );
        storedTask.setWorkload(taskData.getWorkload());
        storedTask.setDeadline(taskData.getDeadline());
        checkDeadline(storedTask);
        final Employee executor = fetchEmployeeWhichActiveMemberOfProject(
                taskData.getExecutorId(), storedTask.getProject().getId()
        );
        storedTask.setExecutor(executor);
        final Employee author = fetchAuthorOfProject(
                authorProfile.getId(), storedTask.getProject().getId()
        ).getEmployee();
        storedTask.setAuthor(author);
        storedTask.setTitle(taskData.getTitle());
        storedTask.setDescription(taskData.getDescription());
        storedTask.setUpdatedAt(Instant.now());
        repository.save(storedTask);
    }

    public FoundTasksDTO findTasksByFilter(@NonNull FindTasksDTO filter) {
        final List<FoundTasksDTO.FoundTaskDTO> foundTasks = repository.
                findAll(findTaskSpecificationProvider.getObject(filter))
                .stream()
                .map(findTasksMapper::mapToResult)
                .toList();
        return new FoundTasksDTO(foundTasks);
    }

    public ChangedTaskStatusDTO advanceTask(@NonNull ChangeTaskStatusDTO taskData) {
        final Task storedTask = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, taskData::getId, () -> new EntityNotExistsException(
                        "Task with id %d doesn't exist and so can't be advanced".formatted(taskData.getId())
                )
        );
        final TaskStatus advancedStatus = TaskStatus.nextTaskStatus(storedTask.getStatus());
        storedTask.setStatus(advancedStatus);
        repository.save(storedTask);
        return new ChangedTaskStatusDTO(advancedStatus);
    }

    private void checkDeadline(Task task) {
        if (task.getCreatedAt().plus(task.getWorkload(), ChronoUnit.HOURS).isAfter(task.getDeadline())) {
            throw new EarlyDeadlineException(
                    "Created time %s plus workload %s hours should be less than deadline %s"
                            .formatted(task.getCreatedAt(), task.getWorkload(), task.getDeadline())
            );
        }
    }

    private ProjectTeam fetchAuthorOfProject(Long authorId, Long projectId) {
        return projectTeamService
                .getEmployeeInProject(authorId, projectId)
                .orElseThrow(() -> new AuthorNotExistsException(
                        "Employee with id %d isn't member of project with id %s team and so can't be author"
                                .formatted(authorId, projectId)
                ));
    }

    private Employee fetchEmployeeWhichActiveMemberOfProject(Long executorId, @NonNull Long projectId) {
        if (Objects.isNull(executorId)) {
            return null;
        }
        final Employee executorEmployee = employeeService.getEmployeeEntityById(executorId);
        projectTeamService.getEmployeeInProject(executorId, projectId);
        return executorEmployee;
    }
}
