package org.example.status;

import lombok.NonNull;

public enum TaskStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CLOSED;

    public static TaskStatus nextTaskStatus(@NonNull TaskStatus status) {
        return CLOSED.equals(status) ? CLOSED : values()[status.ordinal() + 1];
    }
}
