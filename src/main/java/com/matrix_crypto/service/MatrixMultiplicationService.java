package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public interface MatrixMultiplicationService<T> {
    Matrix<Double> multiply(Matrix<T> a, Matrix<T> b);
}
