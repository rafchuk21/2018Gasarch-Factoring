package util;

import java.util.ArrayList;

public class LinalgUtil {
    /**
     * Adds two rows of an matrix together and places them in a specified index. Modifies the input object
     * @param matrix matrix to operate on
     * @param r1 first row
     * @param r2 second row
     * @param override place to store sum row
     * @return modified matrix
     */
    public static int[][] rowAdd(int[][] matrix, int r1, int r2, int override) {
        int[] newrow = new int[matrix[r1].length];
        for (int i = 0; i < newrow.length; i++) {
            newrow[i] = matrix[r1][i] + matrix[r2][i];
        }
        matrix[override] = newrow;
        return matrix;
    }

    /**
     * Adds two rows of an matrix together and places them in a specified index. Modifies the input object.
     * Replaces r1 with the new row by default.
     * @param matrix matrix to operate on
     * @param r1 first row
     * @param r2 second row
     * @return modified matrix
     */
    public static int[][] rowAdd(int[][] matrix, int r1, int r2) {
        return rowAdd(matrix, r1, r2, r1);
    }

    /**
     * Subtracts two rows of an matrix together and places them in a specified index. Modifies the input object
     * @param matrix matrix to operate on
     * @param r1 first row
     * @param r2 second row
     * @param override place to store sum row
     * @return modified matrix
     */
    public static int[][] rowSubtract(int[][] matrix, int r1, int r2, int override) {
        int[] newrow = new int[matrix[r1].length];
        for (int i = 0; i < newrow.length; i++) {
            newrow[i] = matrix[r1][i] - matrix[r2][i];
        }
        matrix[override] = newrow;
        return matrix;
    }

    /**
     * Subtracts two rows of an matrix together and places them in a specified index. Modifies the input object.
     * Replaces r1 with the new row by default.
     * @param matrix matrix to operate on
     * @param r1 first row
     * @param r2 second row
     * @return modified matrix
     */
    public static int[][] rowSubtract(int[][] matrix, int r1, int r2) {
        return rowAdd(matrix, r1, r2, r1);
    }

    /**
     * Goes through a matrix and mods each element by a given modulo.
     * @param matrix matrix to operate on
     * @param modulo modulo to use
     * @return modified matrix
     */
    public static int[][] matrixMod(int[][] matrix, int modulo) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = matrix[i][j] % modulo;
            }
        }

        return matrix;
    }

    /**
     * Switches two rows in a matrix
     * @param matrix matrix to operate on
     * @param r1 first row
     * @param r2 second row
     * @return modified matrix
     */
    public static int[][] switchRows(int[][] matrix, int r1, int r2) {
        int[] temprow = matrix[r2];
        matrix[r2] = matrix[r1];
        matrix[r1] = temprow;
        return matrix;
    }

    /**
     * Given a matrix of 1s and 0s, puts it into Row Echelon Form
     * @param matrix matrix to operate on
     * @return modified matrix
     */
    public static int[][] rowEchelonForm(int[][] matrix) {
        int numOps = Math.min(matrix.length, matrix[0].length);
        for (int i = 0; i < numOps; i++) {
            if (matrix[i][i] != 1) {
                for (int j = i + 1; j < matrix.length; j++) {
                    if (matrix[j][i] == 1) {
                        matrix = switchRows(matrix, i, j);
                        break;
                    }
                }
            }

            for (int j = i + 1; j < matrix.length; j++) {
                if (matrix[j][i] == 1) {
                    matrix = rowSubtract(matrix, j, i);
                }
            }
        }

        return matrix;
    }

    public static int[][] rowEchelonForm(int[][] matrix, ArrayList<int[]> replacements) {
        int numOps = Math.min(matrix.length, matrix[0].length);
        int[] rep = new int[2];
        for (int i = 0; i < numOps; i++) {
            if (matrix[i][i] != 1) {
                for (int j = i + 1; j < matrix.length; j++) {
                    if (matrix[j][i] == 1) {
                        matrix = switchRows(matrix, i, j);
                        rep[0] = i; rep[1] = j; replacements.add(rep.clone());
                        break;
                    }
                }
            }
            matrixMod(matrix, 2);
            for (int j = i + 1; j < matrix.length; j++) {
                if (matrix[j][i] == 1) {
                    matrix = rowSubtract(matrix, j, i);
                    matrixMod(matrix, 2);
                }
            }
        }
        matrixMod(matrix, 2);
        return matrix;
    }

    public static int[][] remove0Columns(int[][] matrix, ArrayList<Integer> removals) {
        for (int i = 0; i < matrix[0].length; i++) {
            boolean allZeroes = true;
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != 0) {
                    allZeroes = false;
                    break;
                }
            }

            if (allZeroes) {
                removals.add(i);
                int[][] tempMatrix = new int[matrix.length][matrix[i].length - 1];
                for (int j = 0; j < tempMatrix.length; j++) {
                    for (int k = 0; k < tempMatrix[j].length; k++) {
                        if (k < i) {
                            tempMatrix[j][k] = matrix[j][k];
                        }
                        if (k >= i) {
                            tempMatrix[j][k] = matrix[j][k + 1];
                        }
                    }
                }
                matrix = tempMatrix;
            }
        }

        return matrix;
    }
}
