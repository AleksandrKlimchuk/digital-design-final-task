package org.example.service.integrated;

import org.example.service.integrated.utils.ProjectTeamTestEntitiesFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.lang3.RandomUtils;
import org.example.dto.project_team.AddEmployeeDTO;
import org.example.dto.project_team.FindAllEmployeesDTO;
import org.example.exception.ProjectNotExistsException;
import org.example.service.ProjectTeamService;
import org.example.status.ProjectTeamRole;
import org.example.store.model.Employee;
import org.example.store.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
@SpringBootTest(classes = Main.class)
public class AddAvailableEmployeeInUnavailableProjectTest extends PostgresTestBase {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProjectTeamService projectTeamService;

    Employee employee1 = ProjectTeamTestEntitiesFactory.createEmployee1();

    @BeforeEach
    public void init() {
        employee1 = employeeRepository.save(employee1);
    }

    @Test
    @DisplayName("Добавить сотрудника в недоступную группу")
    @Tag("integrated")
    public void test() {
        final Long generatedProjectId = RandomUtils.nextLong(1, Long.MAX_VALUE);
        Assertions.assertThrows(
                ProjectNotExistsException.class,
                () -> projectTeamService.addEmployeeToProject(
                        new AddEmployeeDTO(generatedProjectId, employee1.getId(), ProjectTeamRole.ANALYST)
                ),
                "Проект с id " + generatedProjectId + " существует"
        );
        Assertions.assertThrows(
                ProjectNotExistsException.class,
                () -> projectTeamService.getAllEmployeesOfProject(new FindAllEmployeesDTO(generatedProjectId)),
                "Проект с id " + generatedProjectId + " существует"
        );
    }


    @AfterEach
    public void destroy() {
        employeeRepository.delete(employee1);
    }
}
