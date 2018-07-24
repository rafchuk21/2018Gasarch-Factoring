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
     * @param upperBound upper bound of random, exclusive
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
     * @param lowerBound lower bound of random, inclusive
     * @param upperBound upper bound of random, exclusive
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

    /**
     * Returns (a^b)%c
     * @param a  BigInteger input
     * @param b BigInteger input
     * @param c BigInteger input
     * @return BigInteger (a^b)%c
     */
    public static BigInteger exponentialMod(BigInteger a, BigInteger b, BigInteger c) {
        if (b.compareTo(BigInteger.ZERO) == 0) {return BigInteger.ONE;}
        BigInteger tempMultiple = BigInteger.ONE;
        for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE)) {
            tempMultiple = tempMultiple.multiply(a).mod(c);
        }
        return tempMultiple;
    }

    /**
     * Returns the length of a BigInteger expressed in base 10
     * @param a BigInteger input
     * @return length of a
     */
    public static BigInteger bigIntLength(BigInteger a) {
        return BigInteger.valueOf(a.toString().length());
    }
}
