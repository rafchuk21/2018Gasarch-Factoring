package factoring;

import com.sun.deploy.util.ArrayUtil;
import primes.PrimeGenerator;
import util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class QuadraticSieve {
    private BigInteger n;
    private int B;
    private boolean time;
    private ArrayList<Long> primes;
    private ArrayList<Long> factorBase;
    private int numPrimes;
    private int[][] matrix;
    private ArrayList<Row> initialRows;

    public QuadraticSieve(boolean time) {
        this.time = time;
    }
    public QuadraticSieve() {
        this(false);
    }

    public ArrayList<Integer> factor(BigInteger n, int B) {
        this.n = n;
        this.B = B;
        primes = (ArrayList<Long>) PrimeGenerator.sieveOfEratosthenes(10000).getResult();
        factorBase = buildFactorBase(B);
        System.out.println(factorBase);
        numPrimes = factorBase.size();
        matrix = new int[numPrimes+1][numPrimes];
        initialRows = new ArrayList<>();
        buildMatrix();



        return null;
    }

    private BigInteger getNextNum(BigInteger m) {
        return m.add(BigInteger.ONE);
    }

    /**
     * Checks if a number is a product of numbers in the factor base
     * @param m number to test
     * @return whether number is product of numbers in factor base
     */
    private boolean checkNum(BigInteger m) {
        for (long p : factorBase) {
            while (m.mod(BigInteger.valueOf(p)).compareTo(BigInteger.ZERO) == 0) {
                m = m.divide(BigInteger.valueOf(p));
            }
        }

        return m.compareTo(BigInteger.ONE) == 0;
    }

    /**
     * Finds the Legendre Symbol (n/p). If there exists an x s.t. N = x^2 mod p, returns 1. Otherwise, returns p-1.
     * @param p prime to check
     * @return Legendre Symbol (n/p)
     */
    private int legendreSymbol(long p) {
        return n.modPow(BigInteger.valueOf((p-1)/2), BigInteger.valueOf(p)).intValue();
    }

    /**
     * Generates a list of all the primes less than maxFactor, where there exists an x s.t. N = x^2 mod p.
     * @param maxFactor largest factor in the factor base, i.e. the B-limit
     * @return Factor base
     */
    private ArrayList<Long> buildFactorBase(int maxFactor) {
        ArrayList<Long> ret = new ArrayList<>();
        for (long p : primes) {
            if (p > maxFactor) {break;}
            if (legendreSymbol(p) == 1 && p != 2) {
                ret.add(p);
            }
        }
        return ret;
    }

    /**
     * Builds the matrix. Each row is a mod 2 vector of the power of each factor in the factor base for m^2 mod N, where
     * m^2 mod N is a product of numbers in the factor base.
     */
    private void buildMatrix() {
        BigInteger m = BigIntegerUtils.sqrt(n).add(BigInteger.ONE);
        int numRows = 0;
        while (numRows < numPrimes + 1) {
            //System.out.println("Attempting to build new row");
            BigInteger k = m.modPow(BigInteger.valueOf(2), n);
            if (checkNum(k)) {
                ArrayList<Integer> vector = buildFactorVector(k);
                initialRows.add(new Row(numRows, m, k, vector));
                matrix[numRows] = intArrayListToIntArray(vector);
                numRows++;
            }
            m = getNextNum(m);
        }
    }

    /**
     * Given a number which is the product of numbers in the factor base, generates the mod 2 vector of the power of
     * each of its factors.
     * @param m Number to generate factor vector of
     * @return mod 2 factor vector of m
     */
    private ArrayList<Integer> buildFactorVector(BigInteger m) {
        ArrayList<Integer> vector = new ArrayList<>();
        for (long p : factorBase) {
            vector.add(0);
            while (m.mod(BigInteger.valueOf(p)).intValue() == 0) {
                m = m.divide(BigInteger.valueOf(p));
                vector.set(vector.size()-1, vector.get(vector.size()-1) + 1);
            }
            vector.set(vector.size()-1, vector.get(vector.size()-1) % 2);
        }
        return vector;
    }

    /**
     * Converts an ArrayList<Integer> to an int[]
     * @param a ArrayList to convert
     * @return a as an int[]
     */
    private int[] intArrayListToIntArray(ArrayList<Integer> a) {
        int[] ret = new int[a.size()]; int i = 0;
        for (int l : a) {
            ret[i] = l;
            i++;
        }
        return ret;
    }

    /**
     * Constructs a string containing the information of each of the initial rows.
     * @return String representation of the initial matrix.
     */
    public String initialMatrixToString() {
        String r = "";
        for (int i = 0; i < matrix.length; i++) {
            String l = "";
            Row currRow = initialRows.get(i);
            l += currRow.getIndex();
            while (l.length() < 4) { l += " ";} l+= currRow.getM();
            while (l.length() < 18) {l += " ";} l+= currRow.getFm();
            while (l.length() < 40) {l += " ";} l += currRow.getFactorVector().toString() + "\n";
            r += l;
        }
        return r;
    }

    /**
     * Converts the current matrix to a string
     * @return String form of matrix
     */
    public String matrixToString() {
        String r = "";
        for (int i = 0; i < matrix.length; i++) {
            int[] currRow = matrix[i];
            r += Arrays.toString(currRow) + "\n";
        }
        return r;
    }

    public ArrayList<Long> getFactorBase() {
        return factorBase;
    }

    private class Row {
        private int index;
        private BigInteger m;
        private BigInteger fm;
        private ArrayList<Integer> factorVector;
        public Row(int i, BigInteger m, BigInteger fm, ArrayList<Integer> factorVector) {
            this.index = i;
            this.m = m;
            this.fm = fm;
            this.factorVector = factorVector;
        }
        public int getIndex() {
            return index;
        }
        public BigInteger getM() {
            return m;
        }
        public BigInteger getFm() {
            return fm;
        }
        public ArrayList<Integer> getFactorVector() {
            return factorVector;
        }
    }
}
