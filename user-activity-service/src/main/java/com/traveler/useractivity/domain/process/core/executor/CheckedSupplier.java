package com.traveler.useractivity.domain.process.core.executor;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
