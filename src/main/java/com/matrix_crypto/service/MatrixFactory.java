package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public interface MatrixFactory<T> {
    Matrix<T> create(int height, int width);
    Matrix<T> createFromString(String serializedMatrix);
    Matrix<T> createInvertableMatrix(int size);
}
