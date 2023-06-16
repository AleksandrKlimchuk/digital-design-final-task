package org.example.service.integrated;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.example.dto.project_team.ExcludeEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.exception.EntityNotExistsException;
import org.example.mapper.project_team.FindAllEmployeeMapper;
import org.example.service.ProjectTeamService;
import org.example.service.integrated.utils.ProjectTeamTestEntitiesFactory;
import org.example.status.ProjectTeamRole;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.model.ProjectTeam;
import org.example.store.repository.EmployeeRepository;
import org.example.store.repository.ProjectRepository;
import org.example.store.repository.ProjectTeamRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = Main.class)
public class ExcludeEmployeeFromProjectTest extends PostgresTestBase {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectTeamRepository projectTeamRepository;
    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    FindAllEmployeeMapper findAllEmployeeMapper;

    Employee employee1 = ProjectTeamTestEntitiesFactory.createEmployee1();
    Employee employee2 = ProjectTeamTestEntitiesFactory.createEmployee2();

    Project projectA = ProjectTeamTestEntitiesFactory.createProjectA();

    ProjectTeam employee1InProjectA;
    ProjectTeam employee2InProjectA;

    @BeforeEach
    @Transactional
    public void init() {
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

        projectA = projectRepository.save(projectA);

        employee1InProjectA = projectTeamRepository.save(new ProjectTeam(projectA, employee1, ProjectTeamRole.ANALYST));
        employee2InProjectA = projectTeamRepository.save(new ProjectTeam(projectA, employee2, ProjectTeamRole.TESTER));
    }

    @Test
    @DisplayName("Исключить сотрудника из проекта")
    @Tag("integrated")
    public void test() {
        Assertions.assertDoesNotThrow(
                () -> projectTeamService.excludeEmployeeFromProject(
                        new ExcludeEmployeeDTO(projectA.getId(), employee2.getId())
                ),
                "Сотрудник 2 не был удален из проекта А"
        );
        Assertions.assertThrows(
                EntityNotExistsException.class,
                () -> projectTeamService.excludeEmployeeFromProject(
                        new ExcludeEmployeeDTO(projectA.getId(), employee2.getId())
                ),
                "Сотрудник 2 не был ранее удален из проекта А"
        );
        final Long generatedProjectId = RandomUtils.nextLong(2, Long.MAX_VALUE);
        Assertions.assertThrows(
                EntityNotExistsException.class,
                () -> projectTeamService.excludeEmployeeFromProject(
                        new ExcludeEmployeeDTO(generatedProjectId, employee1.getId())
                ),
                "Группа " + generatedProjectId + " существует"
        );
        final Long generatedEmployeeId = RandomUtils.nextLong(3, Long.MAX_VALUE);
        Assertions.assertThrows(
                EntityNotExistsException.class,
                () -> projectTeamService.excludeEmployeeFromProject(
                        new ExcludeEmployeeDTO(projectA.getId(), generatedEmployeeId)
                ),
                "Сотрудник " + generatedEmployeeId + " существует"
        );
        final FoundAllEmployeesDTO foundAllEmployeesOfProjectA = Assertions.assertDoesNotThrow(
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(projectA.getId())),
                "Не получилось получить сотрудников проекта A"
        );
        Assertions.assertEquals(
                1, foundAllEmployeesOfProjectA.getFoundEmployees().size(), "Неожиданный размер команды проекта A"
        );
        final List<FoundAllEmployeesDTO.FoundProjectTeamEmployee> expectedEmployeesOfGroupA = List.of(
                findAllEmployeeMapper.mapToResult(employee1InProjectA)
        );
        Assertions.assertTrue(
                CollectionUtils.isEqualCollection(
                        expectedEmployeesOfGroupA, foundAllEmployeesOfProjectA.getFoundEmployees()
                ), "Неожиданный состав команды проекта А"
        );
    }

    @AfterEach
    @Transactional
    public void destroy() {
        projectTeamRepository.delete(employee1InProjectA);
        projectTeamRepository.delete(employee2InProjectA);

        employeeRepository.delete(employee1);
        employeeRepository.delete(employee2);

        projectRepository.delete(projectA);
    }
}
