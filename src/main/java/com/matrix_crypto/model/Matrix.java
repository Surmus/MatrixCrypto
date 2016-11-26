package com.matrix_crypto.model;

public class Matrix<T> {

    private T[][] container;

    @SuppressWarnings({"unchecked"})
    public Matrix(int height, int width) {
        container = (T[][]) new Object[height][width];
    }

    public void set(int row, int column, T value) {
        container[row][column] = value;
    }

    public T get(int row, int column) {
        return container[row][column];
    }

    public int getHeight() {
        return container.length;
    }

    public int getWidth() {
        return container[0].length;
    }

    /**
     * Creates transposed matrix from This
     */
    public Matrix<T> transpose() {
        Matrix<T> transposedMatrix = new Matrix<T>(getWidth(), getHeight());

        //Populate the new matrix with values from this matrix
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                transposedMatrix.set(i, j, get(j, i));
            }
        }

        return transposedMatrix;
    }

    public Matrix<T> extract(int excludeRow, int excludeColumn) {
        //Validate
        if (getHeight() - 1 < 0 || getWidth() - 1 < 0) {
            throw new IllegalArgumentException("Cannot extract smaller matrix from empty matrix");
        }

        if (excludeRow >= getHeight() || excludeColumn >= getWidth()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Exclude arguments must fit into bounds of the matrix(%s, %s)",
                            getHeight(),
                            getWidth()
                    )
            );
        }

        Matrix<T> smallerMatrix = new Matrix<T>(getHeight() - 1, getWidth() - 1);

        int rowCounter = 0;

        for (int i = 0; i < getHeight(); i++) {
            int columnCounter = 0;

            //Exclude this row entirely from the new matrix
            if (i == excludeRow) {
                continue;
            }

            for (int j = 0; j < getWidth(); j++) {
                //Exclude this column entirely from the new matrix
                if (j == excludeColumn) {
                    continue;
                }

                smallerMatrix.set(rowCounter, columnCounter, get(i, j));
                columnCounter++;
            }

            rowCounter++;
        }

        return smallerMatrix;
    }

    /**
     * Check if the matrix contains any NULL values
     */
    public boolean isFull()
    {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (get(i, j) == null) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String output = "";

        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                output += get(i, j);

                if (j < (getWidth() - 1)) {
                    output += " ";
                }
            }

            if (i < (getHeight() - 1)) {
                output += "\n";
            }
        }

        return output;
    }

}
