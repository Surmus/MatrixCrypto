package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public interface MatrixInvertService<T> {
    Matrix<Double> invert(Matrix<T> originalMatrix);
}
