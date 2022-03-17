package com.capitan.encuestas.utils.transformer;

public interface Transformer<K, T> {
    T transformData(K data);
}
