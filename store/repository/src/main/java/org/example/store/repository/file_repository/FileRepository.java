package org.example.store.repository.file_repository;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.example.store.model.Id;
import org.example.store.repository.Repository;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class FileRepository<EntityType extends Id<? extends EntityType>> implements Repository<EntityType> {

    private static final String REPOSITORY_ROOT_DIRECTORY = ".data/";
    private static final String SERVICE_STORAGE_EXTENSION = ".srv";
    private static final String ID_SERVICE_STORAGE_NAME = "id";
    private static final Long DEFAULT_INITIAL_ID = 1L;

    private final String repositoryPath;
    private final File idStorage;
    private final Class<EntityType> entityTypeClass;

    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    protected FileRepository() {
        this.entityTypeClass = (Class<EntityType>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        this.repositoryPath = REPOSITORY_ROOT_DIRECTORY + this.entityTypeClass.getName() + "/";
        final File repositoryDirectory = new File(this.repositoryPath);
        repositoryDirectory.mkdirs();
        this.idStorage = createIdStorageIfNotExists(this.repositoryPath);
    }

    @SneakyThrows
    @Override
    public EntityType create(@NonNull EntityType entity) {
        if (!Objects.isNull(entity.getId())) {
            throw new IllegalArgumentException("Save entity %s interrupted. To save entity, id must be unspecified");
        }
        Long availableId = readObject(idStorage, Long.class);
        final File entityStorage = new File(repositoryPath + availableId);
        if (!entityStorage.createNewFile()) {
            throw new RuntimeException("Structure of storage was corrupted");
        }
        entity = entity.withId(availableId);
        writeObject(entityStorage, entity);
        writeObject(idStorage, availableId + 1L);
        return entity;
    }

    @Override
    public EntityType update(@NonNull EntityType entity) {
        if (Objects.isNull(entity.getId())) {
            throw new IllegalArgumentException("Update entity %s interrupted. To save entity, id must be specified");
        }
        final File entityStorage = new File(repositoryPath + entity.getId());
        if (!entityStorage.exists() || !entityStorage.isFile()) {
            throw new IllegalArgumentException("Entity can't be updated before it's created");
        }
        writeObject(entityStorage, entity);
        return entity;
    }

    @Override
    public Optional<? extends EntityType> getById(@NonNull Long id) {
        final File requiredUserStorage = new File(repositoryPath + id);
        if (!requiredUserStorage.exists() || !requiredUserStorage.isFile()) {
            return Optional.empty();
        }
        return Optional.of(readObject(requiredUserStorage, entityTypeClass));
    }

    @Override
    public List<? extends EntityType> getAll() {
        final File repositoryDirectory = new File(repositoryPath);
        File[] storages = repositoryDirectory.listFiles((dir, name) -> !name.endsWith(SERVICE_STORAGE_EXTENSION));
        if (Objects.isNull(storages)) {
            return Collections.emptyList();
        }
        return Arrays.stream(storages)
                .map(storage -> readObject(storage, entityTypeClass))
                .toList();
    }

    @Override
    public EntityType deleteById(@NonNull Long id) {
        final EntityType entityToDelete = getById(id).orElseThrow(() -> new IllegalArgumentException(
                "Entity with id %d of class %s doesn't exist".formatted(id, entityTypeClass.getName())
        ));
        final File storageToDelete = new File(repositoryPath + id);
        if (storageToDelete.delete()) {
            throw new RuntimeException("File in path %s can't be deleted".formatted(storageToDelete.getAbsolutePath()));
        }
        return entityToDelete;
    }

    @SneakyThrows
    private static File createIdStorageIfNotExists(@NonNull String availableIdStoragePath) {
        final File availableIdStorage = new File(
                availableIdStoragePath + ID_SERVICE_STORAGE_NAME + SERVICE_STORAGE_EXTENSION
        );
        if (!availableIdStorage.exists() || !availableIdStorage.isFile()) {
            if (!availableIdStorage.createNewFile()) {
                throw new RuntimeException(
                        "File %s can't be created".formatted(availableIdStorage.getAbsoluteFile())
                );
            }
            writeObject(availableIdStorage, DEFAULT_INITIAL_ID);
        }
        return availableIdStorage;
    }

    @SneakyThrows
    private static <T> T readObject(@NonNull File sourceFile, @NonNull Class<? extends T> objectType) {
        final Object extractedObject;
        try (var entityInputStream = new ObjectInputStream(new FileInputStream(sourceFile))) {
            extractedObject = entityInputStream.readObject();
        }
        return objectType.cast(extractedObject);
    }

    @SneakyThrows
    private static void writeObject(@NonNull File destinationFile, @NonNull Object entity) {
        try (var employeeObjectOutputStream = new ObjectOutputStream(new FileOutputStream(destinationFile))) {
            employeeObjectOutputStream.writeObject(entity);
        }
    }
}
