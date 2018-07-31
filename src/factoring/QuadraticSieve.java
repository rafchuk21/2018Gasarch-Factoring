package factoring;

import primes.PrimeGenerator;
import util.BigIntegerUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;

public class QuadraticSieve {
    private BigInteger n;
    private int B;
    private boolean time;
    private ArrayList<Long> primes;
    private ArrayList<Long> factorBase;
    private ArrayList<ArrayList<BigInteger>> factorBaseRoots;
    private int numPrimes;
    private int[][] matrix;
    private ArrayList<Row> initialRows;

    private final BigInteger ZERO = BigInteger.ZERO;
    private final BigInteger ONE = BigInteger.ONE;
    private final BigInteger TWO = BigInteger.valueOf(2);
    private final BigInteger TEN = BigInteger.TEN;

    public QuadraticSieve(boolean time) {
        this.time = time;
    }
    public QuadraticSieve() {
        this(false);
    }

    public ArrayList<Integer> factor(BigInteger n, int B) {
        this.n = n;
        this.B = B;
        primes = PrimeGenerator.sieveOfEratosthenes(10000).getResult();
        factorBase = buildFactorBase(B);
        System.out.println(factorBase);
        buildFactorBaseRoots();
        System.out.println(Arrays.deepToString(factorBaseRoots.toArray()));
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
        for (long p : factorBase) {                                                 //for every prime in the factor base
            while (m.mod(BigInteger.valueOf(p)).compareTo(BigInteger.ZERO) == 0) { //while you can divide m by that prime
                m = m.divide(BigInteger.valueOf(p));                                //divide m by that prime
            }
        }

        return m.compareTo(BigInteger.ONE) == 0;     //if the remainder is 1, m was a product of primes in factor base
    }

    /**
     * Finds the Legendre Symbol (n/p). If there exists an x s.t. N = x^2 mod p, returns 1. Otherwise, returns p-1.
     * @param p prime to check
     * @return Legendre Symbol (n/p)
     */
    private int legendreSymbol(BigInteger k, BigInteger p) {
        int ls = k.modPow(p.subtract(ONE).divide(TWO), p).intValue(); //System.out.println(p + ", " + ls);
        return ls;
    }

    private int legendreSymbol(long k, long p) { return legendreSymbol(BigInteger.valueOf(k), BigInteger.valueOf(p)); }
    private int legendreSymbol(long p) { return legendreSymbol(n, BigInteger.valueOf(p)); }

    /**
     * Generates a list of all the primes less than maxFactor, where there exists an x s.t. N = x^2 mod p.
     * @param maxFactor largest factor in the factor base, i.e. the B-limit
     * @return Factor base
     */
    private ArrayList<Long> buildFactorBase(int maxFactor) {
        ArrayList<Long> ret = new ArrayList<>();
        for (long p : primes) {
            if (p > maxFactor) {break;}
            if (legendreSymbol(p) == 1) {
                ret.add(p);
            }
        }
        return ret;
    }

    private ArrayList<ArrayList<BigInteger>> buildFactorBaseRoots() {
        factorBaseRoots = new ArrayList<>();
        for (long p : factorBase) {
            ArrayList<BigInteger> r = modRoot(p);
            System.out.println(r + "^2 == " + n + " mod " + p);
            factorBaseRoots.add(r);
        }
        return factorBaseRoots;
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
                System.out.println(initialRows.get(initialRows.size()-1).toString());
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
     * Solves for x s.t. x^2 == N (mod p) using the Tonelli-Shanks algorithm
     * @param p to solve in the congruency
     * @return all solutions for x
     */
    private ArrayList<BigInteger> modRoot(BigInteger p) {
        if (legendreSymbol(p.longValue()) != 1) {throw new InputMismatchException();}

        ArrayList<BigInteger> solutions = new ArrayList<>();
        BigInteger q = p.subtract(ONE); BigInteger s = ZERO;
        while (q.mod(TWO).compareTo(ZERO) == 0) {
            s = s.add(ONE);
            q = q.divide(TWO);
        }
        if (s.compareTo(ONE) == 0) {
            BigInteger r = n.modPow(p.add(ONE).divide(BigInteger.valueOf(4)), p);
            solutions.add(r);
            solutions.add(p.subtract(r));
            return solutions;
        }
        BigInteger z = TWO;
        while (legendreSymbol(z, p) != p.subtract(ONE).intValueExact()) { z = z.add(ONE); }
        BigInteger c = z.modPow(q, p);
        BigInteger r = n.modPow(q.add(ONE).divide(TWO), p);
        BigInteger t = n.modPow(q, p);
        BigInteger m = s;

        while (true) {
            if (t.equals(ONE)) { solutions.add(r); solutions.add(p.subtract(r)); return solutions; }
            BigInteger i = ZERO;
            BigInteger b = t;
            while (!b.equals(ONE) && i.compareTo(m) < 0) {
                //System.out.println("b " + b + ", i " + i + ", m " + m);
                b = b.modPow(TWO, p);
                i = i.add(ONE);
            }

            BigInteger u = c;
            BigInteger e = m.subtract(i).subtract(ONE);
            while (e.compareTo(ZERO) > 0) {
                //System.out.println("u");
                u = u.modPow(TWO, p);
                e = e.subtract(ONE);
            }

            r = r.multiply(u).mod(p);
            c = u.modPow(TWO, p);
            t = t.multiply(c).mod(p);
            m = i;

            //System.out.println("p: " + p + ", r " + r + ", c " + c + ", t " + t + ", m " + m);
        }
    }

    private ArrayList<BigInteger> modRoot(long p) {
        return modRoot(BigInteger.valueOf(p));
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
        
        public String toString() {
            String r = "";
            r += this.getIndex();
            while (r.length() < 4) { r += " ";} r+= this.getM();
            while (r.length() < 18) {r += " ";} r+= this.getFm();
            while (r.length() < 40) {r += " ";} r += this.getFactorVector().toString();
            return r;
        }
    }
}
