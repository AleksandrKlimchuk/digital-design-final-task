package org.example.api.utils.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeSwaggerDescriptions {

    public final String CREATE_EMPLOYEE_DESCRIPTION = """
            Создает профиль нового сотрудника с перечисленным в теле запроса набором атрибутов и статусом Активный.""";

    public final String CREATE_EMPLOYEE_SUCCESS_RESPONSE = """
            Профиль сотрудника создан успешно. В теле ответа размещен id созданного профиля.""";

    public final String CREATE_EMPLOYEE_ALREADY_EXISTS = """
            Уже существует активный сотрудник с указанным значением учетной записи.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_EMPLOYEE_DESCRIPTION = """
            Изменяет профиль сотрудника в соответствии с аттрибутами, переданными в теле запроса.
            Изменить можно только сотрудника со статусом Активный.""";

    public final String UPDATE_EMPLOYEE_SUCCESS_RESPONSE = """
            Профиль сотрудника обновлен успешно.""";

    public final String UPDATE_EMPLOYEE_NOT_EXISTS = """
            Попытка обновить сотрудника по id, который не соответствует ни одному сотруднику.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_EMPLOYEE_CONFLICT = """
            Попытка обновить сотрудника, который либо был ранее удален,
            либо переданная учетная запись неуникальна среди сотрудников.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String DELETE_EMPLOYEE_DESCRIPTION = """
            Удаление профиля сотрудника (первод в статус Удаленный)""";

    public final String DELETE_EMPLOYEE_SUCCESS_RESPONSE = """
            Профиль сотрудника успешно удален.""";

    public final String DELETE_EMPLOYEE_NOT_EXISTS = """
            Попытка удалить сотрудника по id, который не соответствует ни одному сотруднику.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String GET_EMPLOYEES_BY_FILTER_DESCRIPTION = """
            Поиск осуществляется по текстовому значению (по полям Фамилия, Имя, Отчество,
            Учетная запись, Адрес электронной почты) и только среди активных сотрудников. Если фильтр не задан,
            то возвращаются все сотрудники""";

    public final String GET_EMPLOYEES_BY_FILTER_SUCCESS = """
            В теле ответа возвращается список всех сотрудников, удовлетворяющих фильтру.""";

    public final String GET_EMPLOYEE_BY_ID_DESCRIPTION = """
            Поиск сотрудника по id. Поиск осущетсвляется среди всех сотрудников всех статусов""";

    public final String GET_EMPLOYEE_BY_IDENTIFIER_SUCCESS = """
            Сотрудник найден. Данные о сотруднике возвращаются в теле ответа""";

    public final String GET_EMPLOYEE_BY_ID_NOT_FOUND = """
            Сотрудник с переданным уникальным идентификатором не найден.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String GET_EMPLOYEE_BY_ACCOUNT_DESCRIPTION = """
            Поиск сотрудника по учетной записи. Поиск осущетсвляется среди активных сотрудников""";

    public final String GET_EMPLOYEE_BY_ACCOUNT_NOT_FOUND = """
            Сотрудник с переданной учетной записью не найден.""" + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;
}
