package factoring;

import util.BigIntegerUtils;
import java.math.BigInteger;
import java.util.Arrays;

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
        if (x.multiply(x).compareTo(n) < 0) {x = x.add(BigInteger.ONE);}
        BigInteger t = x.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE);
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
     * Uses Pollard's Rho method to find 2 factors of a given n
     * @param n number to factor
     * @return Array containing the two found factors, or 1 and n if none found
     */
    public static BigInteger[] pollardRhoFactoring(BigInteger n) {
        long startTime = System.nanoTime();
        BigInteger[] factors = new BigInteger[2];
        BigInteger b = BigIntegerUtils.randBigInteger(BigInteger.ONE, n.subtract(BigInteger.valueOf(2)));
        BigInteger s = BigIntegerUtils.randBigInteger(n);
        BigInteger A = s; BigInteger B = s;
        BigInteger g = BigInteger.ONE;
        System.out.println(b + ", " + s);
        while (g.compareTo(BigInteger.ONE) == 0) {
            //System.out.println(A + ", " + B);
            A = (A.multiply(A).add(b)).mod(n);
            B = b.add((B.multiply(B).add(b)).mod(n).pow(2)).mod(n);
            g = A.subtract(B).gcd(n);
        }
        if (g.compareTo(n) < 0) {factors[0] = g; factors[1] = n.divide(g);}
        else {factors[1] = n; factors[0] = BigInteger.ONE;}
        long endTime = System.nanoTime();
        System.out.println("Pollard runtime: " + (endTime - startTime) + " nanoseconds");
        return factors;
    }

    public static void main(String[] args) {
        //System.out.println(Arrays.toString(diffSquareFactoring(new BigInteger("52866631"))));
        //System.out.println(Arrays.toString(diffSquareFactoring(new BigInteger("1743035045201245231"))));
        BigInteger[] pollardRhoFactoring = pollardRhoFactoring(new BigInteger("1743035045201245231"));
        System.out.println(Arrays.toString(pollardRhoFactoring));
        while (pollardRhoFactoring[0].compareTo(BigInteger.ONE) == 0) {
            pollardRhoFactoring = pollardRhoFactoring(new BigInteger("1743035045201245231"));
            System.out.println(Arrays.toString(pollardRhoFactoring));
        }
    }
}
