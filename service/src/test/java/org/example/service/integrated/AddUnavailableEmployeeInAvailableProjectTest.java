package org.example.service.integrated;

import org.apache.commons.lang3.RandomUtils;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.dto.project_team.FoundAllEmployeesDTO;
import org.example.exception.EmployeeDeletedException;
import org.example.exception.EmployeeNotExistsException;
import org.example.service.ProjectTeamService;
import org.example.service.integrated.utils.ProjectTeamTestEntitiesFactory;
import org.example.status.EmployeeStatus;
import org.example.status.ProjectTeamRole;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.repository.EmployeeRepository;
import org.example.store.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Main.class)
public class AddUnavailableEmployeeInAvailableProjectTest extends PostgresTestBase {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectTeamService projectTeamService;

    Employee employee1 = ProjectTeamTestEntitiesFactory.createEmployee1();

    Project projectA = ProjectTeamTestEntitiesFactory.createProjectA();

    @BeforeEach
    @Transactional
    public void init() {
        employee1.setStatus(EmployeeStatus.DELETED);
        employee1 = employeeRepository.save(employee1);

        projectA = projectRepository.save(projectA);
    }

    @Test
    @DisplayName("Добавить недоступного сотрудника в доступный проект")
    @Tag("integrated")
    public void test() {
        Assertions.assertThrows(
                EmployeeDeletedException.class,
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectA.getId(), employee1.getId(), ProjectTeamRole.ANALYST)
                ),
                "Сотрудник 1 не был ранее удален"
        );
        final Long generatedEmployeeId = RandomUtils.nextLong(2, Long.MAX_VALUE);
        Assertions.assertThrows(
                EmployeeNotExistsException.class,
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(projectA.getId(), generatedEmployeeId, ProjectTeamRole.ANALYST)
                ),
                "Сотрудник с id " + generatedEmployeeId + " существует"
        );
        final FoundAllEmployeesDTO foundAllEmployeesOfProjectA = Assertions.assertDoesNotThrow(
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(projectA.getId())),
                "Не получилось получить сотрудников проекта A"
        );
        Assertions.assertTrue(
                foundAllEmployeesOfProjectA.getFoundEmployees().isEmpty(), "Команда проекта A не пуста"
        );
    }

    @AfterEach
    @Transactional
    public void destroy() {
        employeeRepository.delete(employee1);

        projectRepository.delete(projectA);
    }
}
