package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public interface MatrixCryptService<T> {
    String getCipherText(Matrix<T> salt, String plaintext);
    String getPlaintext(Matrix<T> salt, String plaintext);
}
