package primes;

import util.*;
import java.math.BigInteger;
import java.util.Random;

public class PrimeTester {

    /**
     * Returns (a^b)%c
     * @param a  BigInteger input
     * @param b BigInteger input
     * @param c BigInteger input
     * @return BigInteger (a^b)%c
     */
    public static BigInteger exponentialMod(BigInteger a, BigInteger b, BigInteger c) {
        BigInteger tempMultiple = BigInteger.ONE;
        for (BigInteger i = BigInteger.ZERO; i.compareTo(b) < 0; i = i.add(BigInteger.ONE)) {
            tempMultiple = tempMultiple.multiply(a).mod(c);
        }
        return tempMultiple;
    }

    /**
     * Conducts Fermat's Primality Test, which uses the fact that if p is prime, (a^(p-1))%p = 1 (probably). More
     * iterations -> wider diversity of a's, which makes the test less likely to fail
     * @param p Potential prime
     * @param iterations number of a's to test
     * @return boolean conclusion of test
     */
    public static boolean fermatPrimalityTest(BigInteger p, int iterations) {
        if (p.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }
        BigInteger random;
        for (int i = 0; i < iterations; i++) {
            random = BigIntegerUtils.randBigInteger(BigInteger.ONE, p); //get random BigInteger on [1,p-1]
            if (!(exponentialMod(random, p.subtract(BigInteger.ONE), p).compareTo(BigInteger.ONE) == 0)) { //if (a^(p-1))%p != 1
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        //System.out.println(exponentialMod(BigInteger.valueOf(14), BigInteger.valueOf(10), BigInteger.valueOf(6)).toString());
        System.out.println(fermatPrimalityTest(BigInteger.valueOf(417), 30));
    }

}
