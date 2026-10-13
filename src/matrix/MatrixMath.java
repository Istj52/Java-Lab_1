package matrix;

import complex.Complex;
import complex.ComplexMath;

public final class MatrixMath {
    private static final double EPSILON = 1e-10;
    private MatrixMath() {}

    public static Matrix add(Matrix a, Matrix b) {
        int rowsA = a.getRows();
        int colsA = a.getCols();
        int rowsB = b.getRows();
        int colsB = b.getCols();
        if (rowsA == rowsB && colsA == colsB)
        {
            Matrix result = new Matrix(rowsA, colsA);
            for (int i = 0; i < rowsA; ++i) {
                for (int j = 0; j < colsA; ++j) {
                    result.set(i, j, ComplexMath.add(a.get(i, j), b.get(i, j)));
                }
            }
            return result;
        }
        else throw new IllegalArgumentException("Измерения не совпадают");
    }
    public static Matrix mul(Matrix a, Matrix b) {
        int rowsA = a.getRows();
        int colsA = a.getCols();
        int rowsB = b.getRows();
        int colsB = b.getCols();
        if (rowsB == colsA) {
            Matrix result = new Matrix(rowsA, colsB);
            for (int i = 0; i < rowsA; ++i) {
                for (int j = 0; j < colsB; ++j) {
                    Complex sum = new Complex(0, 0);
                    for (int k = 0; k < colsA; ++k)
                    {
                        Complex multiple = ComplexMath.mul(a.get(i, k), b.get(k, j));
                        sum = ComplexMath.add(sum, multiple);
                    }
                    result.set(i, j, sum);
                }
            }
            return result;
        }
        else throw new IllegalArgumentException("Измерения не позволяют перемножить матрицы");
    }
    public static Matrix div(Matrix a, Matrix b) {
        return mul(a, inverse(b));
    }
    public static Matrix transpose(Matrix a) {
        int rows = a.getRows();
        int cols = a.getCols();
        Matrix result = new Matrix(cols, rows);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                result.set(j, i, a.get(i, j));
            }
        }
        return result;
    }
    public static Complex determinant(Matrix a) {
        int n = a.getRows();
        if (n != a.getCols()) {
            throw new IllegalArgumentException("Матрица должна быть квадратной");
        }
        if (n == 1) { return a.get(0, 0); }
        if (n == 2) {
            Complex a1 = ComplexMath.mul(a.get(0, 0), a.get(1, 1));
            Complex a2 = ComplexMath.mul(a.get(0, 1), a.get(1, 0));
            return ComplexMath.sub(a1, a2);
        }
        Complex det = new Complex(0, 0);
        for (int i = 0; i < n; ++i) {
            Matrix sub = getSubMatrix(a, 0, i);
            Complex step = ComplexMath.mul(a.get(0, i), determinant(sub));
            if (i % 2 == 0) {
                det = ComplexMath.add(det, step);
            }
            else {
                det = ComplexMath.sub(det, step);
            }
        }
        return  det;
    }
    private static Matrix getSubMatrix(Matrix a, int row, int col) {
        int n = a.getRows();
        Matrix sub = new Matrix(n - 1, n - 1);
        int r = 0;
        for (int i = 0; i < n; ++i) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < n; ++j) {
                if (j == col) continue;
                sub.set(r, c, a.get(i, j));
                ++c;
            }
            r++;
        }
        return sub;
    }
    private static Matrix inverse(Matrix a) {
        int n = a.getRows();
        if (n != a.getCols()) {
            throw new IllegalArgumentException("Матрица должна быть квадратной");
        }
        Matrix aug = new Matrix(n, 2 * n);
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < n; ++j)
            {
                aug.set(i, j, a.get(i, j));
            }
            aug.set(i, i + n, new Complex(1.0, 0));
        }
        for (int i = 0; i < n; ++i) {
            int maxRow = i;
            double max = ComplexMath.abs(aug.get(i, i));
            for (int r = i + 1; r < n; ++r) {
                double cur = ComplexMath.abs(aug.get(r, i));
                if (cur > max) {
                    max = cur;
                    maxRow = r;
                }
            }
            if (max < EPSILON) {
                throw new IllegalArgumentException("Матрица вырождена");
            }
            if (maxRow != i) {
                for (int j = 0; j < 2 * n; ++j) {
                    Complex t = aug.get(i, j);
                    aug.set(i, j, aug.get(maxRow, j));
                    aug.set(maxRow, j, t);
                }
            }
            Complex pivot = aug.get(i, i);
            for (int j = 0; j < 2 * n; ++j) {
                aug.set(i, j, ComplexMath.div(aug.get(i, j), pivot));
            }
            for (int j = 0; j < n; ++j) {
                if (j != i) {
                    Complex factor = aug.get(j, i);
                    for (int k = 0; k < 2 * n; ++k) {
                        Complex sub = ComplexMath.mul(factor, aug.get(i, k));
                        aug.set(j, k, ComplexMath.sub(aug.get(j, k), sub));
                    }
                }
            }
        }
        Matrix result = new Matrix(n, n);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                result.set(i, j, aug.get(i, j + n));
            }
        }
        return result;
    }
}
