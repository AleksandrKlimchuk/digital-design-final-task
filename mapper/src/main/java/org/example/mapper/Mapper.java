package org.example.mapper;

public interface Mapper<EntityType, IncomingDTO, ResultDTO> extends
        ToEntityMapper<EntityType, IncomingDTO>,
        ToResultMapper<EntityType, ResultDTO> {
}
