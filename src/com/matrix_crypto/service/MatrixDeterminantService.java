package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public interface MatrixDeterminantService<T> {
    Double getDeterminant(Matrix<T> matrix);
}
