package org.example.store.model;

public interface Id<T> {
    Long getId();

    T withId(Long id);
}
