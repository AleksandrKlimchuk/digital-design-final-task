package org.example.mapper;

import lombok.NonNull;

public interface ToEntityMapper<EntityType, IncomingDTO> {
    EntityType mapToEntity(@NonNull IncomingDTO dto);

}
