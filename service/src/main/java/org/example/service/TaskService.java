package org.example.service;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.task.*;
import org.example.mapper.task.CreateTaskMapper;
import org.example.mapper.task.FindTasksMapper;
import org.example.service.utils.ServiceUtils;
import org.example.status.ProjectStatus;
import org.example.status.TaskStatus;
import org.example.store.model.Employee;
import org.example.store.model.ProjectTeam;
import org.example.store.model.Task;
import org.example.store.repository.TaskRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public CreatedTaskDTO createTask(@NonNull CreateTaskDTO taskData) {
        final ProjectTeam authorEmployeeInProject = projectTeamService.getEmployeeInProject(
                taskData.getAuthorId(), taskData.getProjectId()
        );
        final Employee executorEmployee = fetchExecutorWhichActiveMemberOfProject(
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

    public void updateTask(@NonNull UpdateTaskDTO taskData) {
        final Task storedTask = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, taskData::getId, () -> new IllegalArgumentException(
                        "Task with id %d doesn't exist and so can't be updated".formatted(taskData.getId())
                )
        );
        storedTask.setWorkload(taskData.getWorkload());
        storedTask.setDeadline(taskData.getDeadline());
        checkDeadline(storedTask);
        final Employee executor = fetchExecutorWhichActiveMemberOfProject(
                taskData.getExecutorId(), storedTask.getProject().getId()
        );
        storedTask.setExecutor(executor);
        final Employee author = fetchExecutorWhichActiveMemberOfProject(
                taskData.getAuthorId(), storedTask.getProject().getId()
        );
        storedTask.setAuthor(author);
        storedTask.setTitle(taskData.getTitle());
        storedTask.setDescription(taskData.getDescription());
        storedTask.setUpdatedAt(Instant.now());
        repository.save(storedTask);
    }

    public FoundTasksDTO findTasksByFilter(@NonNull FindTasksDTO filter) {
        final List<FoundTasksDTO.FoundTaskDTO> foundTasks = repository.
                findAll(getFilterSpecification(filter))
                .stream()
                .map(findTasksMapper::mapToResult)
                .toList();
        return new FoundTasksDTO(foundTasks);
    }

    public ChangedTaskStatusDTO advanceTask(@NonNull ChangeTaskStatusDTO taskData) {
        final Task storedTask = ServiceUtils.fetchEntityByIdOrThrow(
                repository::findById, taskData::getId, () -> new IllegalArgumentException(
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
            throw new IllegalArgumentException(
                    "Created time %s plus workload %s hours should be less than deadline %s"
                            .formatted(task.getCreatedAt(), task.getWorkload(), task.getDeadline())
            );
        }
    }

    private Employee fetchExecutorWhichActiveMemberOfProject(Long executorId, @NonNull Long projectId) {
        if (Objects.isNull(executorId)) {
            return null;
        }
        final Employee executorEmployee = employeeService.getEmployeeEntityById(executorId);
        projectTeamService.getEmployeeInProject(executorId, projectId);
        return executorEmployee;
    }

    private Specification<Task> getFilterSpecification(FindTasksDTO filter) {
        return (root, query, criteriaBuilder) -> {
            final String textFilter = "%" + filter.getTitle() + "%";
            Predicate finalPredicate = criteriaBuilder.like(root.get("title"), textFilter);
            if (!Objects.isNull(filter.getStatuses()) && !filter.getStatuses().isEmpty()) {
                final Expression<ProjectStatus> statusExpression = root.get("status");
                final Predicate status = statusExpression.in(filter.getStatuses());
                finalPredicate = criteriaBuilder.and(finalPredicate, status);
            }
            if (!Objects.isNull(filter.getExecutorId())) {
                final Predicate executor = criteriaBuilder.equal(
                        root.get("executor").get("id"), filter.getExecutorId()
                );
                finalPredicate = criteriaBuilder.and(finalPredicate, executor);
            }
            if (!Objects.isNull(filter.getAuthorId())) {
                final Predicate author = criteriaBuilder.equal(
                        root.get("author").get("id"), filter.getAuthorId()
                );
                finalPredicate = criteriaBuilder.and(finalPredicate, author);
            }
            if (!Objects.isNull(filter.getDeadline())) {
                final Predicate deadline = criteriaBuilder.lessThanOrEqualTo(
                        root.get("deadline"), filter.getDeadline()
                );
                finalPredicate = criteriaBuilder.and(finalPredicate, deadline);
            }
            if (!Objects.isNull(filter.getCreatedAt())) {
                final Predicate createdAt = criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), filter.getCreatedAt()
                );
                finalPredicate = criteriaBuilder.and(finalPredicate, createdAt);
            }
            return query
                    .where(finalPredicate)
                    .getRestriction();
        };
    }
}
