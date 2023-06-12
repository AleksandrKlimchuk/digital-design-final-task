package org.example.api.utils.swagger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskSwaggerDescriptions {

    public final String CREATE_TASK_DESCRIPTION = """
            Создает задачу с перечисленным в теле запроса набором атрибутов и статусом Новая""";

    public final String CREATE_TASK_SUCCESS_RESPONSE = """
            Задача создана успешно. В теле ответа размещен id созданной задачи.""";

    public final String CREATE_TASK_BAD_REQUEST = """
            Попытка создать задачу с неверным крайним сроком выполнения."""
            + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String CREATE_TASK_NOT_FOUND = """
            Попытка создать задачу для проекта, который не существует."""
            + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_TASK_DESCRIPTION = """
            Изменяет созданную задачу аттрибутами, переданными в теле запроса.""";


    public final String UPDATE_TASK_SUCCESS_RESPONSE = """
            Задача изменена успешно.""";

    public final String UPDATE_TASK_BAD_REQUEST = """
            Попытка обноваить задачу неверным крайним сроком выполнения."""
            + ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String UPDATE_TASK_NOT_FOUND = """
            Попытка обновить задачу которая не сущетсвует или проектом который не существует.""" +
            ErrorSwaggerDescription.ERROR_DETAILS_IN_BODY;

    public final String GET_TASKS_BY_FILTER_DESCRIPTION = """
            Поиск осущетсвляется по текстовому фильтру (по полям Исполнитель, Автор задачи, по периоду крайнего срока,
            по периоду даты создания) и с применением фильтра по статусу задачи. Если ни один из фильтров не задан,
            то возвращаются все созданные задачи""";

    public final String GET_TASKS_BY_FILTER_SUCCESS = """
            В теле ответа возвращается список всех задач, удовлетворяющих фильтру.""";

    public final String CHANGE_STATUS_DESCRIPTION = """
            Переводит задачу на следующий этап""";

    public final String CHANGE_STATUS_SUCCESS_RESPONSE = """
            Статус задачи успешно изменен, в теле ответа размещен новый статус""";

    public final String CHANGE_STATUS_NOT_EXISTS = """
            Попытка перевести статус для задачи по id, который не соответствует ни одному проекту""";
}
