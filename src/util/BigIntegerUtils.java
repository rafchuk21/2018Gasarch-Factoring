package util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class BigIntegerUtils {
    /**
     * Finds the square root of a BigInteger
     * @param n BigInteger to find the square root of
     * @return Square root of n
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
        while (b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1);
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }

    /**
     * generates a random BigInteger on [0, upperBound)
     * @param upperBound
     * @return
     */
    public static BigInteger randBigInteger(BigInteger upperBound) {
        Random rnd = new Random();
        BigInteger r;
        do {
            r = new BigInteger(upperBound.bitLength(), rnd);
        } while (r.compareTo(upperBound) >= 0);
        return r;
    }

    /**
     * generates a random BigInteger on [lowerBound, upperBound)
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public static BigInteger randBigInteger(BigInteger lowerBound, BigInteger upperBound) {
        return randBigInteger(upperBound.subtract(lowerBound)).add(lowerBound);
    }

    public static boolean isEven(BigInteger n) {
        return (n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0);
    }

    public static void main(String[] args) {
        int n = 12;
        int[] results = new int[n + 1];
        for (int i = 0; i < 100; i++) {
            System.out.println(i + ": " + sqrt(BigInteger.valueOf(i)).toString());
        }
    }
}
