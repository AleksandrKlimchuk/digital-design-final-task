package org.example.status;

import lombok.NonNull;

public enum ProjectStatus {
    DRAFT,
    DEVELOP,
    TEST,
    COMPLETE;

    public static ProjectStatus nextProjectStatus(@NonNull ProjectStatus status) {
        return COMPLETE.equals(status) ? COMPLETE : values()[status.ordinal() + 1];
    }
}
