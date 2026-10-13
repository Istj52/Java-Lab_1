package matrix;

import complex.Complex;

public final class Matrix {
    private final int rows;
    private final int cols;
    private final Complex[][] matrix;
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];
        for (int i = 0; i < rows; ++i)
        {
            for (int j = 0; j < cols; ++j)
            {
                matrix[i][j] = new Complex(0, 0);
            }
        }
    }
    public Complex get(int i, int j) {
        return matrix[i][j];
    }
    public void set(int i, int j, Complex complex) {
        matrix[i][j] = complex;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
}