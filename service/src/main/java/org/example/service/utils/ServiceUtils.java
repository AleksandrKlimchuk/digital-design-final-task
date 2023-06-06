package org.example.service.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public class ServiceUtils {

    public <EntityType, IdType>
    EntityType fetchEntityByIdOrThrow(
            @NonNull Function<IdType, Optional<EntityType>> entityExtractor,
            @NonNull Supplier<IdType> idSupplier,
            @NonNull Supplier<? extends RuntimeException> exceptionSupplier
    ) {
        return entityExtractor
                .apply(idSupplier.get())
                .orElseThrow(exceptionSupplier);
    }

    public <EntityType, ValueType>
    void checkValueIsUniqueWithPredicateOrThrow(
            @NonNull Function<ValueType, Optional<EntityType>> entityExtractor,
            @NonNull Supplier<ValueType> valueSupplier,
            @NonNull Predicate<EntityType> nonUniquePredicate,
            @NonNull Supplier<? extends RuntimeException> exceptionSupplier
    ) {
        entityExtractor
                .apply(valueSupplier.get())
                .ifPresent(
                        entity -> {
                            if (nonUniquePredicate.test(entity)) {
                                throw exceptionSupplier.get();
                            }
                        }
                );
    }
}
