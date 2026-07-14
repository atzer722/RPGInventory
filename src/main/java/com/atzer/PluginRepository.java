package com.atzer;

import java.util.Optional;

public interface PluginRepository<U, V> {

    U save(U obj);
    Optional<U> findById(V id);
    boolean delete(U obj);
    U update(U obj);
}