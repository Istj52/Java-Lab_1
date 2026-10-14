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
            try {
                Operation operation = askOperation(scanner);
                Matrix result;
                switch (operation) {
                    case EXIT: {
                        return;
                    }
                    case DETERMINANT: {
                        System.out.println("Детерминант = " + executeScalarOperation(scanner, null));
                        break;
                    }
                    default: {
                        result = executeMatrixOperation(scanner, operation, null);
                        System.out.println("Результат:\n" + result);
                        while (askContinue(scanner)) {
                            Operation operate = askOperation(scanner);
                            if (operate == Operation.EXIT) {
                                return;
                            }
                            else if (operate == Operation.DETERMINANT) {
                                if (result.getRows() == result.getCols()) {
                                    System.out.println("Детерминант = " + executeScalarOperation(scanner, result));
                                    break;
                                }
                                System.out.println("Матрица должна быть квадратной");
                                continue;
                            }
                            result = executeMatrixOperation(scanner, operate, result);
                            System.out.println("Результат:\n" + result);
                        }
                        break;
                    }
                }
            } catch (MenuRequestedException e) { System.out.println("Возврат в главное меню"); }
        }
    }

    private static boolean askContinue(Scanner scanner) {
        System.out.println("Если вы хотите произвести другую операцию над результатом, введите 1, иначе любое другое целое число");
        while (true) {
            try {
                int input = scanner.nextInt();
                return input == 1;
            } catch (InputMismatchException e) {
                String inpt = scanner.nextLine();
                if (inpt.trim().equals("q")) {
                    throw new MenuRequestedException();
                }
                System.out.println("Недопустимый Формат, попробуйте снова");
            }
        }
    }

    private enum Operation {
        EXIT, ADDITION, MULTIPLICATION, DIVISION, TRANSPOSE, DETERMINANT
    }

    private static class MenuRequestedException extends RuntimeException {}


    private static Operation askOperation(Scanner scanner) {
        Operation[] ops = Operation.values();
        System.out.println("Введите номер операции (вы сможете вернуться сюда в любой момент, введя q):");
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
                String input = scanner.nextLine();
                if (input.trim().equals("q")) {
                    throw new MenuRequestedException();
                }
                System.out.println("Недопустимый формат, попробуйте снова");
            }
        }
    }

    private static Matrix executeMatrixOperation(Scanner scanner, Operation operation, Matrix res) {
        while (true) {
            switch (operation) {
                case ADDITION: {
                    int[] dimensionsA = res != null ? new int[] {res.getRows(), res.getCols()} :checkMatrixDimensions(scanner, 1);
                    int[] dimensionsB = checkMatrixDimensions(scanner, 2);
                    if (Arrays.equals(dimensionsA, dimensionsB)) {
                        Matrix a = res != null ? res : createMatrix(scanner, dimensionsA[0], dimensionsA[1], 1);
                        Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1], 2);
                        return MatrixMath.add(a, b);
                    }
                    System.out.println("Размеры матриц должны совпадать");
                    break;
                }
                case MULTIPLICATION: {
                    int[] dimensionsA = res != null ? new int[] {res.getRows(), res.getCols()} :checkMatrixDimensions(scanner, 1);
                    int[] dimensionsB = checkMatrixDimensions(scanner, 2);
                    if (dimensionsA[1] == dimensionsB[0]) {
                        Matrix a = res != null ? res : createMatrix(scanner, dimensionsA[0], dimensionsA[1], 1);
                        Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1], 2);
                        return MatrixMath.mul(a, b);
                    }
                    System.out.println("Измерения не позволяют перемножить матрицы");
                    break;
                }
                case DIVISION: {
                    int[] dimensionsA = res != null ? new int[] {res.getRows(), res.getCols()} :checkMatrixDimensions(scanner, 1);
                    int[] dimensionsB = checkMatrixDimensions(scanner, 2);
                    if (dimensionsA[1] == dimensionsB[0] && dimensionsB[0] == dimensionsB[1]) {
                        Matrix a = res != null ? res : createMatrix(scanner, dimensionsA[0], dimensionsA[1], 1);
                        while (true) {
                            try {
                                Matrix b = createMatrix(scanner, dimensionsB[0], dimensionsB[1], 2);
                                return MatrixMath.div(a, b);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage() + ", попробуйте снова");
                            }
                        }
                    }
                    System.out.println("Измерения не позволяют разделить матрицы");
                    break;
                }
                case TRANSPOSE: {
                    int[] dimensions = res != null ? new int[] {res.getRows(), res.getCols()} :checkMatrixDimensions(scanner, 1);
                    Matrix a = res != null ? res : createMatrix(scanner, dimensions[0], dimensions[1], 1);
                    return MatrixMath.transpose(a);
                }
            }
        }
    }

    private static Complex executeScalarOperation(Scanner scanner, Matrix res) {
        while (true) {
            int[] dimensions = res != null ? new int[] {res.getRows(), res.getCols()} :checkMatrixDimensions(scanner, 1);
            if (dimensions[0] == dimensions[1]) {
                Matrix a = res != null ? res : createMatrix(scanner, dimensions[0], dimensions[1], 1);
                return MatrixMath.determinant(a);
            }
            System.out.println("Матрица должна быть квадратной");
        }
    }
    private static int[] checkMatrixDimensions(Scanner scanner, int n) {
        while (true) {
            int rows;
            while (true) {
                System.out.print("Введите количество строк " + n + " матрицы: ");
                try {
                    rows = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    String input = scanner.nextLine();
                    if (input.trim().equals("q")) {
                        throw new MenuRequestedException();
                    }
                    System.out.println("Недопустимый формат, попробуйте снова");
                }
            }
            int cols;
            while (true) {
                System.out.print("Введите количество столбцов " + n + " матрицы: ");
                try {
                    cols = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    String input = scanner.nextLine();
                    if (input.trim().equals("q")) {
                        throw new MenuRequestedException();
                    }
                    System.out.println("Недопустимый формат, попробуйте снова");
                }
            }
            if (rows > 0 && cols > 0) {
                return new int[]{rows, cols};
            }
            else {
                System.out.println("Количество строк и столбцов должно быть положительным");
            }
        }
    }
    private static Matrix createMatrix(Scanner scanner, int rows, int cols, int n) {
        Matrix m = new Matrix(rows, cols);
        System.out.println("Введите элементы " + n + " матрицы: ");
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                double real, im;
                while (true) {
                    try {
                        System.out.print("[" + i + "][" + j + "] (real im): ");
                        real = scanner.nextDouble();
                        im = scanner.nextDouble();
                        break;
                    } catch (InputMismatchException e) {
                        String input = scanner.nextLine();
                        if (input.trim().equals("q")) {
                            throw new MenuRequestedException();
                        }
                        System.out.println("Недопустимый формат, попробуйте снова");
                    }
                }
                m.set(i, j, new Complex(real, im));
            }
        }
        return m;
    }
}
