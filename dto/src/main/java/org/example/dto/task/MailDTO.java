package org.example.dto.task;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
public class MailDTO {
    @NonNull
    String toMail;
    @NonNull
    String subject;
    @NonNull
    Map<String, Object> context;
}
