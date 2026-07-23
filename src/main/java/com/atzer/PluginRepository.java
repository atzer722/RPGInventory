package com.atzer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class PluginRepository<U, V> {

    public abstract void init();
    public abstract CompletableFuture<U> save(U obj);
    public abstract CompletableFuture<Optional<U>> findById(V id);
    public abstract CompletableFuture<Boolean> delete(U obj);
    public abstract CompletableFuture<U> update(U obj);
}