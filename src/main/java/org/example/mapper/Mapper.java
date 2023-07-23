package org.example.mapper;

public interface Mapper<F, T> {

    T map(F source);

    default T map(F source, T target) {
        return target;
    }
}
