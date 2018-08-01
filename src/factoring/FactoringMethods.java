package factoring;

import primes.PrimeGenerator;
import util.BigIntegerUtils;
import util.Results;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static java.math.BigInteger.ONE;

public class FactoringMethods {
    /**
     * Finds 2 factors of N using Fermat's Difference of Squares method
     * @param n number to factor
     * @return Array containing the two found factors, or 1 and n if none found
     */
    public static BigInteger[] diffSquareFactoring(BigInteger n) {
        long startTime = System.nanoTime();
        BigInteger[] factors = new BigInteger[2];
        if (n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
            factors[0] = BigInteger.valueOf(2);
            factors[1] = n.divide(BigInteger.valueOf(2));
            return factors;
        }
        BigInteger x = BigIntegerUtils.sqrt(n);
        if (x.multiply(x).compareTo(n) < 0) {x = x.add(ONE);}
        BigInteger t = x.multiply(BigInteger.valueOf(2)).add(ONE);
        BigInteger r = x.multiply(x).subtract(n);
        while (r.compareTo(BigIntegerUtils.sqrt(r).pow(2)) != 0 && BigIntegerUtils.sqrt(r).compareTo(n) <= 0) { //while r is not square
            r = r.add(t);
            t = t.add(BigInteger.valueOf(2));
        }
        x = t.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
        BigInteger y = BigIntegerUtils.sqrt(r);
        factors[0] = x.subtract(y);
        factors[1] = x.add(y);
        long endTime = System.nanoTime();
        System.out.println("Difference of Squares runtime:" + (endTime - startTime) + " nanoseconds");
        return factors;
    }

    /**
     * Uses Difference of Squares method but as a Results with optional timing
     * @param n Number to factor
     * @param time Whether or not to measure time
     * @return Results containing elapsed time and an array containing two factors of n
     */
    public static Results<BigInteger[]> diffSquareFactoring(BigInteger n, boolean time) {
        if (!time) return new Results<>(-1, -1, diffSquareFactoring(n), "N/A", "Factors");

        long startTime = System.nanoTime();
        BigInteger[] factors = diffSquareFactoring(n);
        long endTime = System.nanoTime();
        return new Results<>(endTime-startTime, -1, factors, "N/A", "Factors");
    }

    /**
     * Uses Pollard's Rho method to find 2 factors of a given n
     * @param n number to factor
     * @return Array containing the two found factors, or 1 and n if none found
     */
    public static BigInteger[] pollardRhoFactoring(BigInteger n) {
        BigInteger[] factors = new BigInteger[2];
        BigInteger b = BigIntegerUtils.randBigInteger(ONE, n.subtract(BigInteger.valueOf(2)));
        BigInteger s = BigIntegerUtils.randBigInteger(n);
        BigInteger A = s; BigInteger B = s;
        BigInteger g = ONE;
        System.out.println(b + ", " + s);
        while (g.compareTo(ONE) == 0) {
            //System.out.println(A + ", " + B);
            A = (A.multiply(A).add(b)).mod(n);
            B = b.add((B.multiply(B).add(b)).mod(n).pow(2)).mod(n);
            g = A.subtract(B).gcd(n);
        }
        if (g.compareTo(n) < 0) {factors[0] = g; factors[1] = n.divide(g);}
        else {factors[1] = n; factors[0] = ONE;}
        return factors;
    }

    /**
     * Uses Pollard's Rho method but as a Results with optional timing
     * @param n Number to factor
     * @param time Whether or not to measure time
     * @return Results containing elapsed time and an array containing two factors of n
     */
    public static Results<BigInteger[]> pollardRhoFactoring(BigInteger n, boolean time) {
        if (!time) return new Results<>(-1, -1, pollardRhoFactoring(n), "N/A", "Factors");

        long startTime = System.nanoTime();
        BigInteger[] factors = pollardRhoFactoring(n);
        long endTime = System.nanoTime();
        return new Results<>(endTime-startTime, -1, factors, "N/A", "Factors");
    }

    public static BigInteger[] pollardRhoMinusOneFactoring(BigInteger n, long B) {
        BigInteger[] factors = new BigInteger[2];
        ArrayList<Long> primes = PrimeGenerator.sieveOfEratosthenes(B).getResult(); //all primes from 2 to B
        //System.out.println("Primes Generated " + primes.toString());
        BigInteger a = BigInteger.valueOf(2);
        BigInteger g;
        BigInteger t;
        for (int i = 0; i < primes.size(); i++) {
            int e = (int) (Math.log(B)/Math.log(primes.get(i)));
            BigInteger f = BigInteger.valueOf(primes.get(i)).pow(e);
            a = a.modPow(f, n);
            t = a.subtract(ONE).gcd(n);
            if (t.compareTo(ONE) != 0 && t.compareTo(n) != 0 ) {
                System.out.println(primes.get(i));
                factors[0] = t; factors[1] = n.divide(t);
                return factors;
            }
        }
        factors[0] = ONE; factors[1] = n; return factors;
    }

    /**
     *
     * @param n number to factor
     * @param B bound
     * @return
     */
    public static Results<BigInteger[]> pollardRhoMinusOneFactoring(BigInteger n, long B, boolean time) {
        if (!time) {return new Results<>(-1, -1, pollardRhoMinusOneFactoring(n, B), "N/A", "Factors");}
        long startTime = System.nanoTime();
        BigInteger[] results = pollardRhoMinusOneFactoring(n, B);
        long endTime = System.nanoTime();
        return new Results<>(endTime - startTime, -1, results, "N/A", "Factors");
    }

    public static BigInteger[] quadraticSieve(BigInteger n, long B) {
        ArrayList<Long> primes = PrimeGenerator.sieveOfEratosthenes(B).getResult();
        System.out.println(primes);
        int numPrimes = primes.size();
        int[][] matrix = new int[numPrimes][numPrimes];
        ArrayList<BigInteger> storedNumbers = new ArrayList<>();
        BigInteger m = BigIntegerUtils.sqrt(n).add(BigInteger.ONE);
        BigInteger k; int v;
        int[] currentRow;
        boolean included;
        while (storedNumbers.size() < numPrimes) {
            currentRow = new int[numPrimes];
            k = m.modPow(BigInteger.valueOf(2), n);
            for (int i = 0; i < numPrimes; i++) {
                while (k.mod(BigInteger.valueOf(primes.get(i))).compareTo(BigInteger.ZERO) == 0) {
                    currentRow[i] = (currentRow[i] + 1) % 2;
                    k = k.divide(BigInteger.valueOf(primes.get(i)));
                }
            }

            if (k.compareTo(BigInteger.ONE) == 0) {
                matrix[storedNumbers.size()] = currentRow;
                storedNumbers.add(m);
            }

            m = m.add(BigInteger.ONE);
        }

        return null;
    }

    public static void main(String[] args) {
        QuadraticSieve qs = new QuadraticSieve();
        //354256138673
        System.out.println(qs.factor(new BigInteger("63787"), 100));
    }
}
