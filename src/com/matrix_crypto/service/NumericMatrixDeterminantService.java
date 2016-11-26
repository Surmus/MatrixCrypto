package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public class NumericMatrixDeterminantService implements MatrixDeterminantService<Double> {

    @Override
    public Double getDeterminant(Matrix<Double> matrix) {
        //Validate that given matrix is square shape
        if (matrix.getHeight() != matrix.getWidth()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Cannot find determinant from non square matrix(%s, %s)",
                            matrix.getHeight(),
                            matrix.getWidth()
                    )
            );
        }

        //Determinant of 1x1 matrix is its only member
        if (matrix.getHeight() == 1) {
            return matrix.get(0, 0);
        }

        if (matrix.getHeight() == 2) {
            return  getSmallMatrixDeterminant(matrix);
        }

        double sum = 0.0;

        for (int i = 0; i < matrix.getWidth(); i++) {
            //Toggle add/subtract into sum after each loop
            if (i % 2 > 0) {
                /*
                extract smaller matrix from current one where the current row and column are excluded,
                then recursively call the function until the matrix is at 2x2 size
                 */
                sum -= matrix.get(0, i) * getDeterminant(matrix.extract(0, i));
            } else {
                sum += matrix.get(0, i) * getDeterminant(matrix.extract(0, i));
            }

        }

        return sum;
    }

    /**
     * Calculates Determinant for 2x2 matrix
     */
    private Double getSmallMatrixDeterminant(Matrix<Double> matrix) {
        //Multiply matrix primary diagonal and subtract opposite diagonal
        return (matrix.get(0, 0) * matrix.get(1, 1)) - (matrix.get(0, 1) * matrix.get(1, 0));
    }

}
