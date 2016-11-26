package com.matrix_crypto.service;

import com.matrix_crypto.model.Matrix;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service("matrixFactory")
public class NumericMatrixFactory implements MatrixFactory<Double> {

    private MatrixDeterminantService determinantService;

    public NumericMatrixFactory(MatrixDeterminantService<Double> determinantService) {
        this.determinantService = determinantService;
    }

    @Override
    public Matrix<Double> create(int height, int width) {
        Matrix<Double> newMatrix = new Matrix<>(height, width);

        populate(newMatrix);

        return newMatrix;
    }

    public Matrix<Double> createFromString(String serializedMatrix) {
        Matrix<Double> matrix = null;
        String[] matrixRows = serializedMatrix.split("[\n]");

        //Matrix rows are separated by new line special char \n
        for (int i = 0; i < matrixRows.length; i++) {
            //Columns @ one matrix row are separated by tab character
            String[] rowValues = matrixRows[i].split("[ ]");

            //Now we know the matrix dimensions, lets create new empty matrix
            if (matrix == null) {
                matrix = new Matrix<>(matrixRows.length, rowValues.length);
            }

            for (int j = 0; j < rowValues.length; j++) {
                matrix.set(i, j, Double.valueOf(rowValues[j]));
            }
        }

        return matrix;
    }

    public Matrix<Double> createInvertableMatrix(int size) {
        Matrix<Double> createdMatrix;

        do {
            //Create random square matrix
            createdMatrix = create(size, size);
        } while (determinantService.getDeterminant(createdMatrix) == 0); //Matrix is invertable when determinant is not 0

        return createdMatrix;
    }

    private void populate(Matrix<Double> emptyMatrix) {
        Random r = new Random();
        int min = 1;
        int max = 100;

        //Populate each matrix item with random number
        for (int i = 0; i < emptyMatrix.getHeight(); i++) {
            for (int j = 0; j < emptyMatrix.getWidth(); j++) {
                emptyMatrix.set(i, j, (double) r.nextInt((max - min) + 1) + min);
            }
        }
    }
}
