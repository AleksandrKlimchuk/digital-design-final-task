package org.example.mapper.task;

import lombok.NonNull;
import org.example.dto.task.MailDTO;
import org.example.mapper.ToResultMapper;
import org.example.store.model.Employee;
import org.example.store.model.Project;
import org.example.store.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component
public class MailMapper implements ToResultMapper<Task, MailDTO> {

    @Value("${spring.mail.username}")
    String email;
    @Value("${proxy.mail.enable}")
    Boolean proxyEmail;

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault());

    @Override
    public MailDTO mapToResult(@NonNull Task entity) {
        final Employee executor = Objects.requireNonNull(entity.getExecutor());
        Objects.requireNonNull(executor.getEmail());
        final Employee author = entity.getAuthor();
        final Project project = entity.getProject();
        final String toMail = proxyEmail ? email : executor.getEmail();
        final Map<String, Object> context = new HashMap<>();
        context.put("toMail", executor.getEmail());
        context.put("executorLastName", executor.getLastName());
        context.put("executorFirstName", executor.getFirstName());
        context.put("executorPatronymic", executor.getPatronymic());
        context.put("projectCode", project.getCode());
        context.put("projectTitle", project.getTitle());
        context.put("taskTitle", entity.getTitle());
        context.put("taskDescription", entity.getDescription());
        context.put("taskDeadline", formatter.format(entity.getDeadline()));
        context.put("authorLastName", author.getLastName());
        context.put("authorFirstName", author.getFirstName());
        context.put("authorPatronymic", author.getPatronymic());
        context.put("authorMail", author.getEmail());
        return new MailDTO(toMail, project.getCode() + " " + project.getTitle(), context);
    }
}
