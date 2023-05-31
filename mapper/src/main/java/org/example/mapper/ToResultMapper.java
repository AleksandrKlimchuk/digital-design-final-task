package org.example.mapper;

import lombok.NonNull;

public interface ToResultMapper<EntityType, ResultDTO> {
    ResultDTO mapToResult(@NonNull EntityType entity);
}
