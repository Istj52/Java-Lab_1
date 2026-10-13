import complex.Complex;
import matrix.Matrix;
import matrix.MatrixMath;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Operation operation = askOperation(scanner);
            switch (operation) {
                case EXIT: {
                    return;
                }
                case DETERMINANT: {
                    System.out.println("Детерминант = " + executeScalarOperation(scanner));
                    break;
                }
                default: {
                    System.out.println(executeMatrixOperation(scanner, operation));
                }
            }
        }
    }

    private enum Operation {
        EXIT, ADD, MUL, DIV, TRANSPOSE, DETERMINANT
    }

    private static Operation askOperation(Scanner scanner) {
        Operation[] ops = Operation.values();
        System.out.println("Введите номер операции:");
        for (Operation op : ops) {
            System.out.println(op.name() + " (" + op.ordinal() + ")");
        }
        while (true)
        {
            try {
                int operation = scanner.nextInt();
                if (operation >= 0 && operation < ops.length) {
                    return ops[operation];
                }
                System.out.println("Недопустимое значение, попробуйте снова");
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Недопустимый формат, попробуйте снова");
            }
        }
    }

    private static Matrix executeMatrixOperation(Scanner scanner, Operation operation) {
        while (true) {
            switch (operation) {
                case ADD: {
                    int[] dimensionsA = checkMatrixDimensions(scanner);
                    int[] dimensionsB = checkMatrixDimensions(scanner);
                    if (Arrays.equals(dimensionsA, dimensionsB)) {
                        Matrix a = createMatrix(scanner, dimensionsA[0], dimensionsA[1]);
                        Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1]);
                        return MatrixMath.add(a, b);
                    }
                    System.out.println("Размеры матриц должны совпадать");
                    break;
                }
                case MUL: {
                    int[] dimensionsA = checkMatrixDimensions(scanner);
                    int[] dimensionsB = checkMatrixDimensions(scanner);
                    if (dimensionsA[1] == dimensionsB[0]) {
                        Matrix a = createMatrix(scanner, dimensionsA[0], dimensionsA[1]);
                        Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1]);
                        return MatrixMath.mul(a, b);
                    }
                    System.out.println("Измерения не позволяют перемножить матрицы");
                    break;
                }
                case DIV: {
                    int[] dimensionsA = checkMatrixDimensions(scanner);
                    int[] dimensionsB = checkMatrixDimensions(scanner);
                    if (dimensionsA[1] == dimensionsB[0] && dimensionsB[0] == dimensionsB[1]) {
                        Matrix a = createMatrix(scanner, dimensionsA[0], dimensionsA[1]);
                        Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1]);
                        return MatrixMath.div(a, b);
                    }
                    System.out.println("Измерения не позволяют разделить матрицы");
                    break;
                }
                case TRANSPOSE: {
                    int[] dimensions = checkMatrixDimensions(scanner);
                    Matrix a = createMatrix(scanner, dimensions[0], dimensions[1]);
                    return MatrixMath.transpose(a);
                }
            }
        }
    }

    private static Complex executeScalarOperation(Scanner scanner) {
        while (true) {
            int[] dimensions = checkMatrixDimensions(scanner);
            if (dimensions[0] == dimensions[1]) {
                Matrix a = createMatrix(scanner, dimensions[0], dimensions[1]);
                return MatrixMath.determinant(a);
            }
            System.out.println("Матрица должна быть квадратной");
        }
    }
    private static int[] checkMatrixDimensions(Scanner scanner) {
        while (true) {
            int rows;
            while (true) {
                System.out.print("Введите количество строк: ");
                try {
                    rows = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    scanner.nextLine();
                    System.out.println("Недопустимый формат, попробуйте снова");
                }
            }
            int cols;
            while (true) {
                System.out.print("Введите количество столбцов: ");
                try {
                    cols = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    scanner.nextLine();
                    System.out.println("Недопустимый формат, попробуйте снова");
                }
            }
            if (rows > 0 && cols > 0)
            {
                return new int[]{rows, cols};
            }
            else {
                System.out.println("Количество строк и столбцов должно быть положительным");
            }
        }
    }
    private static Matrix createMatrix(Scanner scanner, int rows, int cols) {
        Matrix m = new Matrix(rows, cols);
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                double real, im;
                while (true) {
                    try {
                        System.out.print("[" + i + "][" + j + "] real im: ");
                        real = scanner.nextDouble();
                        im = scanner.nextDouble();
                        break;
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Недопустимый формат, попробуйте снова");
                    }
                }
                m.set(i, j, new Complex(real, im));
            }
        }
        return m;
    }
}
