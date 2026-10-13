import complex.Complex;
import matrix.Matrix;
import matrix.MatrixMath;

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
            int operation = scanner.nextInt();
            if (operation >= 0 && operation < ops.length) {
                return ops[operation];
            }
            System.out.println("Недопустимое значение, попробуйте снова");
        }
    }

    private static Matrix executeMatrixOperation(Scanner scanner, Operation operation) {
        while (true) {
            try {
                switch (operation) {
                    case ADD: {
                        Matrix a = createMatrix(scanner);
                        Matrix b = createMatrix(scanner);
                        return MatrixMath.add(a, b);
                    }
                    case MUL: {
                        Matrix a = createMatrix(scanner);
                        Matrix b = createMatrix(scanner);
                        return MatrixMath.mul(a, b);
                    }
                    case DIV: {
                        Matrix a = createMatrix(scanner);
                        Matrix b = createMatrix(scanner);
                        return MatrixMath.div(a, b);
                    }
                    case TRANSPOSE: {
                        Matrix a = createMatrix(scanner);
                        return MatrixMath.transpose(a);
                    }
                    default:
                        throw new IllegalStateException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова");
            }
        }
    }

    private static Complex executeScalarOperation(Scanner scanner) {
        while (true) {
            try {
                Matrix a = createMatrix(scanner);
                return MatrixMath.determinant(a);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова");
            }
        }
    }

    private static Matrix createMatrix(Scanner scanner) {
        while (true) {
            System.out.print("Введите количество строк: ");
            int rows = scanner.nextInt();
            System.out.print("Введите количество столбцов: ");
            int cols = scanner.nextInt();
            if (rows > 0 && cols > 0)
            {
                try {
                    Matrix m = new Matrix(rows, cols);
                    System.out.println("Введите элементы матрицы:");
                    for (int i = 0; i < rows; ++i) {
                        for (int j = 0; j < cols; ++j) {
                            System.out.print("[" + i + "][" + j + "] (real, im): ");
                            double real = scanner.nextDouble();
                            double im = scanner.nextDouble();
                            m.set(i, j, new Complex(real, im));
                        }
                    }
                    return m;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    System.out.println("Попробуйте снова");
                }
            }
            else {
                System.out.println("Количество строк и столбцов должно быть положительным");
            }
        }
    }
}
