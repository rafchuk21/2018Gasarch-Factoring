package primes;

import util.*;
import java.math.BigInteger;
import java.util.ArrayList;
import primes.PrimeGenerator;
import java.util.Arrays;

public class PrimeTester {

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
     * Returns (a*b)%c
     * @param a  BigInteger input
     * @param b BigInteger input
     * @param c BigInteger input
     * @return BigInteger (a*b)%c
     */
    public static BigInteger multiplicativeMod(BigInteger a, BigInteger b, BigInteger c) {
        BigInteger x = BigInteger.ZERO, y = a.mod(c);
        while(b.compareTo(BigInteger.ZERO) > 0) {
            if (!BigIntegerUtils.isEven(b)) {
                x = x.add(y).mod(c);
            }
            y = y.multiply(BigInteger.valueOf(2)).mod(c);
            b = b.divide(BigInteger.valueOf(2));
        }
        return x.mod(c);
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

    /**
     * Conducts the Miller-Rabin Primality Test
     * @param p Potential prime
     * @param iterations Number of a's to test
     * @return boolean conclusion of test
     */
    public static boolean millerRabinPrimalityTest(BigInteger p, int iterations) {
        if (p.compareTo(BigInteger.ONE) <= 0) {
            return false;
        }
        if (p.compareTo(BigInteger.valueOf(2)) != 0 && BigIntegerUtils.isEven(p)) {
            return false;
        }
        BigInteger s = p.subtract(BigInteger.ONE); //since p must be odd, p-1 (and s) must be even
        while (BigIntegerUtils.isEven(s)) {
            s = s.divide(BigInteger.valueOf(2)); //s satisfies p-1 = (2^d)*s, ie s is the odd component of p-1
        }
        BigInteger random, temp, mod;
        for (int i = 0; i < iterations; i++) {
            random = BigIntegerUtils.randBigInteger(BigInteger.ONE, p);
            temp = s;
            //mod = exponentialMod(random, temp, p); //a^s%p
            mod = random.modPow(temp, p);
            while (temp.compareTo(p.subtract(BigInteger.ONE)) != 0 && mod.compareTo(p.subtract(BigInteger.ONE)) != 0 &&
                    mod.compareTo(BigInteger.ONE) != 0) {
                mod = mod.multiply(mod).mod(p);
                temp = temp.multiply(BigInteger.valueOf(2));
            }
            if (mod.compareTo(p.subtract(BigInteger.ONE)) != 0 && temp.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean modifiedMillerRabinTest(BigInteger p, int iterations, ArrayList<Long> smallerPrimes) {
        for (Long smallerPrime : smallerPrimes ) {
            if (p.mod(BigInteger.valueOf(smallerPrime)).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
        }
        return millerRabinPrimalityTest(p, iterations);
    }

    public static boolean modifiedMillerRabinTest(BigInteger p, int iterations) {
        return modifiedMillerRabinTest(p, iterations, (ArrayList<Long>) PrimeGenerator.sieveOfEratosthenes(1000).getResult());
    }

    public static void main(String[] args) {
        System.out.println(millerRabinPrimalityTest(BigInteger.valueOf(1000000003), 16));
    }

}
