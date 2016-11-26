package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;

public class NumericMatrixInvertService implements MatrixInvertService<Double> {

    private MatrixDeterminantService<Double> determinantService;

    public NumericMatrixInvertService(MatrixDeterminantService<Double> determinantService) {
        this.determinantService = determinantService;
    }

    @Override
    public Matrix<Double> invert(Matrix<Double> originalMatrix) {
        double determinant = determinantService.getDeterminant(originalMatrix);
        Matrix<Double> invertedMatrix = new Matrix<>(originalMatrix.getHeight(), originalMatrix.getWidth());

        //Validate that the given matrix can be inverted
        if (determinant == 0.0) {
            throw new RuntimeException("Given matrix cannot be inverted");
        }

        //Special case when handling 1x1 matrix, the inverse matrix would be 1 / matrix only value
        if (originalMatrix.getWidth() == 1) {
            invertedMatrix.set(0, 0, 1 / originalMatrix.get(0, 0));

            return invertedMatrix;
        }

        //1. transpose the original matrix
        Matrix<Double> trans = originalMatrix.transpose();

        //2. 1/determinant * (+/-)determinant of every transposed matrix value
        for (int i = 0; i < trans.getHeight(); i++) {

            for (int j = 0; j < trans.getWidth(); j++) {
                Double adjVal = determinantService.getDeterminant(trans.extract(i, j));

                //Toggle sign of the adjval after one matrix value been processed
                if ((i + j) % 2 > 0) {
                    adjVal = -1 * adjVal;
                }

                invertedMatrix.set(
                        i,
                        j,
                        (1 / determinant) * adjVal
                );
            }
        }

        return invertedMatrix;
    }

}
