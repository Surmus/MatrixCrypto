package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public class NumericMatrixMultiplicationService implements MatrixMultiplicationService<Double> {

    @Override
    public Matrix<Double> multiply(Matrix<Double> a, Matrix<Double> b) {
        if (a.getWidth() != b.getHeight()) {
            throw new IllegalArgumentException(
                    String.format(
                    "Matrix a column count(%s) must be equal to the matrix b row count(%s)",
                            a.getWidth(),
                            b.getHeight()
                    )
            );
        }

        Matrix<Double> result = new Matrix<>(a.getHeight(), b.getWidth());

        for (int i = 0; i < a.getHeight(); i++) {

            for (int j = 0; j < b.getWidth(); j++) {
                result.set(i, j, calculateRowValue(a, b, i, j));
            }
        }

        return result;
    }

    private Double calculateRowValue(Matrix<Double> a, Matrix<Double> b, int currentRow, int currentColumn) {
        double rowResult = 0.0;

        for (int i = 0; i < a.getWidth(); i++) {
            rowResult += a.get(currentRow, i) * b.get(i, currentColumn);
        }

        return  rowResult;
    }
}
