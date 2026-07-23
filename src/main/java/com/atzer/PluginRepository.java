package com.atzer;

import java.util.Optional;

public abstract class PluginRepository<U, V> {

    public abstract void init();
    public abstract U save(U obj);
    public abstract Optional<U> findById(V id);
    public abstract boolean delete(U obj);
    public abstract U update(U obj);
}