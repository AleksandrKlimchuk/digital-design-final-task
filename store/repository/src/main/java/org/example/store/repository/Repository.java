package org.example.store.repository;

import lombok.NonNull;
import org.example.store.model.Id;

import java.util.List;
import java.util.Optional;

public interface Repository<EntityType extends Id<? extends EntityType>> {
    EntityType create(@NonNull EntityType entity);
    EntityType update(@NonNull EntityType entity);
    Optional<? extends EntityType> getById(@NonNull Long id);
    List<? extends EntityType> getAll();
    EntityType deleteById(@NonNull Long id);
}
