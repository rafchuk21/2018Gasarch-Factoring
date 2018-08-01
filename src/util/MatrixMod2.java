package util;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixMod2 {
    private int[][] matrix;
    private ArrayList<Integer>[] indices;
    private ArrayList<Integer> zeroRowIndices;
    private boolean[] givenDependencies;

    public MatrixMod2(int[][] matrix) {
        this.matrix = matrix.clone();
        this.indices = (ArrayList<Integer>[]) new ArrayList[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            ArrayList<Integer> rowName = new ArrayList<>();
            rowName.add(i);
            indices[i] = (ArrayList<Integer>) rowName.clone();
        }
    }

    private void swapRows(int r1, int r2) {
        int[] temp = matrix[r1];
        matrix[r1] = matrix[r2];
        matrix[r2] = temp;

        ArrayList<Integer> t = new ArrayList<>(indices[r1]);
        indices[r1] = new ArrayList<>(indices[r2]);
        indices[r2] = t;
    }

    private <T> ArrayList<T> combineArrayLists(ArrayList<T> a1, ArrayList<T> a2) {
        ArrayList<T> combined = new ArrayList<>(a1);
        for (T x : a2) {
            if (!combined.contains(x)) {
                combined.add(x);
            }
        }

        return combined;
    }

    /**
     * In a mod 2 setting, functionally equivalent to subtract rows
     * @param r1 First row
     * @param r2 Second row
     * @param override Row to replace
     */
    private void addRows(int r1, int r2, int override) {
        int[] combined = new int[matrix[r1].length];
        for (int i = 0; i < matrix[r1].length; i++) {
            combined[i] = (matrix[r1][i] + matrix[r2][i]) % 2;
        }

        matrix[override] = combined;
        indices[override] = combineArrayLists(indices[r1], indices[r2]);
    }

    private void addRows(int r1, int r2) {
        addRows(r1, r2, r1);
    }

    public void rref() {
        int i = 0; int j = 0;
        while (i < matrix.length && j < matrix[i].length) {
            while (isZeroColumn(i, j)) {j++; if (j >= matrix[i].length) {break;}}
            if (j >= matrix[i].length) {break;}
            if (matrix[i][j] == 0) {
                for (int k = i + 1; k < matrix.length; k++) {
                    if (matrix[k][j] != 0) {
                        swapRows(i, k);
                    }
                }
            }
            for (int k = 0; k < matrix.length; k++) {
                if (k != i && matrix[k][j] != 0) {
                    addRows(k, i);
                }
            }
            i++; j++;
        }
        //System.out.println(matrixToString());
    }

    private void findZeroRows() {
        zeroRowIndices = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isZeroRow(i)) {zeroRowIndices.add(i);}
        }
    }

    public ArrayList<Integer> findDependency() {
        rref();
        findZeroRows();
        givenDependencies = new boolean[zeroRowIndices.size()];
        givenDependencies[0] = true;
        return composeDependencies();
    }

    public ArrayList<Integer> nextDependency() {
        boolean carry = true;
        for (int i = 0; i < givenDependencies.length; i++) {
            if (i == givenDependencies.length - 1 && carry) {
                throw new IllegalArgumentException();
            } else if (givenDependencies[i] && carry) {
                givenDependencies[i] = false;
                carry = true;
            } else if (!givenDependencies[i] && carry) {
                givenDependencies[i] = true;
                carry = false;
            }
            if (!carry) {break;}
        }

        return composeDependencies();
    }

    private ArrayList<Integer> composeDependencies() {
        ArrayList<Integer> r = new ArrayList<>();
        for (int i = 0; i < givenDependencies.length; i++) {
            if (givenDependencies[i]) {r = combineArrayLists(r, indices[zeroRowIndices.get(i)]);}
        }
        return r;
    }

    private boolean isZeroColumn(int i, int j) {
        for (int k = i; k < matrix.length; k++) {
            if (matrix[k][j] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isZeroRow(int i) {
        for (int k = 0; k < matrix[i].length; k++) {
            if (matrix[i][k] != 0) {
                return false;
            }
        }
        return true;
    }

    public String matrixToString() {
        String r = "";
        for (int i = 0; i < matrix.length; i++) {
            r += Arrays.toString(matrix[i]) + "\t" + indices[i] +  "\n";
        }
        return r;
    }
}
