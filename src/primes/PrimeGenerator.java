package primes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import primes.PrimeTester;
import java.util.Arrays;

import util.*;

public class PrimeGenerator {
    /**
     * Generate primes from 2 to given limit using the naive method
     *
     * @param limit limit for generating primes
     * @return generated primes as an ArrayList of longs
     * @author rafi
     */
    public static Results naivePrimeGenerator(BigInteger limit) {
        if (limit.compareTo(BigInteger.valueOf(2)) < 0) {
            throw new IllegalArgumentException();
        }
        long startTime = System.nanoTime();
        long modCount = 0;
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        primes.add(BigInteger.valueOf(2));
        // for every integer from 3 to the limit
        for (BigInteger i = BigInteger.valueOf(3); i.compareTo(limit) < 0; i = i.add(BigInteger.valueOf(1))) {
            boolean isPrime = true; // assume the number is prime for now
            // for every number already added to the primes list
            for (BigInteger j : primes) {
                modCount++;
                // if the number is divisible by the prime
                if (i.mod(j).equals(BigInteger.ZERO)) {
                    isPrime = false; // mark it as not prime
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            } // if the number never got marked as non-prime, add it to primes list
        }

        long endTime = System.nanoTime();
        return new Results(endTime - startTime, modCount, primes, "modulos", "Primes");
    }

    public static Results naivePrimeGenerator(long limit) {
        if (limit < 2) {
            throw new IllegalArgumentException();
        }
        long startTime = System.nanoTime();
        long modCount = 0;
        ArrayList<Long> primes = new ArrayList<Long>();
        primes.add((long) 2);
        // for every integer from 3 to the limit
        for (long i = 3; i < limit; i++) {
            boolean isPrime = true; // assume the number is prime for now
            // for every number already added to the primes list
            for (long j : primes) {
                modCount++;
                // if the number is divisible by the prime
                if (i % j == 0) {
                    isPrime = false; // mark it as not prime
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            } // if the number never got marked as non-prime, add it to primes list
        }

        long endTime = System.nanoTime();
        return new Results(endTime - startTime, modCount, primes, "modulos", "Primes");
    }

    /**
     * Generate primes from 2 to given limit using the Sieve of Eratosthenes
     * If limit > 2^31, uses another method with a limit up to 2^62
     * @param limit limit for generating primes
     * @return generated primes as an ArrayList of longs
     * @author rafi
     */
    public static Results sieveOfEratosthenes(long limit) {
        if (limit < 2) {
            throw new IllegalArgumentException();
        }
        // if the limit for generating primes is less than the max int value, change to ints
        if (limit < Integer.MAX_VALUE) {
            long startTime = System.nanoTime();
            long operations = 0;
            int intLimit = (int) limit;
            // array representing whether its index is prime or not
            boolean[] numberPrimality = new boolean[intLimit];
            for (int i = 2; i < numberPrimality.length; i++) {
                operations++;
                // assume every number 2 and up is prime
                numberPrimality[i] = true;
            }

            // for every integer from 2 to sqrt(max), if it's prime, cross out its multiples
            for (int i = 2; i < (int) Math.pow(numberPrimality.length, .5) + 1; i++) {
                if (numberPrimality[i]) {
                    for (int j = (int) Math.pow(i,2); j < numberPrimality.length; j += i) {
                        operations++;
                        numberPrimality[j] = false;
                    }
                }
            }

            // compile all the primes into a list
            ArrayList<Long> primes = new ArrayList<Long>();
            for (int i = 2; i < numberPrimality.length; i++) {
                operations++;
                if (numberPrimality[i]) {
                    primes.add((long)i);
                }
            }
            long endTime = System.nanoTime();
            return new Results(endTime - startTime, operations, primes, "array accesses", "Primes");
        } else {
            return largeSieveOfEratosthenes(BigInteger.valueOf(limit));
        }
    }

    public static Results largeSieveOfEratosthenes(BigInteger limit) {
        BigInteger intMaxVal = BigInteger.valueOf(Integer.MAX_VALUE);
        //if (limit.compareTo(intMaxVal) < 0) {
        if (false) {
            return sieveOfEratosthenes(limit.longValue());
        }
        long startTime = System.nanoTime();
        int nrows = limit.divide(intMaxVal).intValue() + 1;
        BigInteger currN = null;
        BitSet[] nPrimality = new BitSet[nrows]; //false = prime, true = composite. Assume all are prime for now
        for (int i = 0; i < nrows; i++) {
            nPrimality[i] = new BitSet(Integer.MAX_VALUE);
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                if (j % 1000000000 == 0) System.out.println(j);
                if (i == 0 && j < 2) j = 2;
                if (!nPrimality[i].get(j)) {
                    currN = BigInteger.valueOf(i).multiply(intMaxVal).add(BigInteger.valueOf(j));
                    System.out.println(currN.toString());
                    for (BigInteger k = currN.pow(2); k.compareTo(limit) < 0; k = k.add(currN)) {
                        if (k.mod(BigInteger.valueOf(100000000)).compareTo(BigInteger.ZERO) == 0) System.out.println(k);
                        nPrimality[k.divide(intMaxVal).intValue()].set(k.mod(intMaxVal).intValue(), true);
                    }
                }
            }
        }

        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        for (int i = 0; i < nrows; i++) {
            for (int j = nPrimality[i].nextClearBit(0); j >= 0; j = nPrimality[i].nextClearBit(j+1)) {
                primes.add(BigInteger.valueOf(i).multiply(intMaxVal).add(BigInteger.valueOf(j)));
            }
            nPrimality[i] = null;
        }

        long endTime = System.nanoTime();

        return new Results(endTime - startTime, 0, primes, "N/A", "Primes");
    }

    /**
     * Generates a list of primes from 2 to limit using the fact that (2n+1) is prime iff n is not of the form i+j+2ij
     * @param limit
     * @return Results of sieve
     */
    public static Results sieveOfSundaram(int limit) {
        if (limit < 2) {
            throw new IllegalArgumentException();
        }
        long startTime = System.nanoTime();
        int arrayAccesses = 0;
        boolean[] nPrimality = new boolean[limit / 2];

        for (int i = 1; i < nPrimality.length; i++) {
            for (int j = 1; i + j + 2*i*j < nPrimality.length && j <= i; j++) {
                nPrimality[i + j + 2*i*j] = true;
                arrayAccesses++;
            }
        }
        ArrayList<Integer> primes = new ArrayList<Integer>();
        primes.add(2);
        for (int i = 1; i < nPrimality.length; i++) {
            if (!nPrimality[i]) {primes.add(i * 2 + 1);}
            arrayAccesses++;
        }
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, arrayAccesses, primes, "array accesses", "Primes");
    }

    /**
     * Generates some primes between given n and 2n
     * @param n
     * @return
     */
    public static Results wheelGenerator(BigInteger n, int wheel, int iterations, int smallerPrimeLimit) {
        System.out.println(n.toString());
        long startTime = System.nanoTime();
        BigInteger wheelBigInt = BigInteger.valueOf(wheel);
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        System.out.println("Coprimes generating");
        Results coprimeResults = CoprimeGenerator.naiveCoprimeGenerator(BigInteger.valueOf(wheel));
        ArrayList<BigInteger> coprimes = (ArrayList<BigInteger>) coprimeResults.getResult();
        System.out.println("Coprimes generated");
        ArrayList<Long> smallerPrimes = (ArrayList<Long>) sieveOfEratosthenes((long) smallerPrimeLimit).getResult();
        for (BigInteger i = n.divide(wheelBigInt); i.multiply(wheelBigInt).compareTo(n.add(n)) < 0; i = i.add(BigInteger.ONE)) {
            System.out.println(i.multiply(wheelBigInt).toString());
            for (BigInteger cp : coprimes) {
                BigInteger p = i.multiply(wheelBigInt).add(cp);
                //System.out.println(p.toString());
                if (p.compareTo(n) > 0) {
                    if (PrimeTester.modifiedMillerRabinTest(p, iterations, smallerPrimes))
                        primes.add(p);
                }
            }
        }
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, 0, primes, "N/A", "Primes");
    }

    /**
     * Generates the first numPrimes primes between given n and 2n
     * @param n
     * @return
     */
    public static Results wheelGenerator(int numPrimes, BigInteger n, int wheel, int iterations, int smallerPrimeLimit) {
        long startTime = System.nanoTime();
        BigInteger wheelBigInt = BigInteger.valueOf(wheel);
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        Results coprimeResults = CoprimeGenerator.naiveCoprimeGenerator(BigInteger.valueOf(wheel));
        ArrayList<BigInteger> coprimes = (ArrayList<BigInteger>) coprimeResults.getResult();
        ArrayList<Long> smallerPrimes = (ArrayList<Long>) sieveOfEratosthenes((long) smallerPrimeLimit).getResult();
        for (BigInteger i = n.divide(wheelBigInt); i.multiply(wheelBigInt).compareTo(n.add(n)) < 0; i = i.add(BigInteger.ONE)) {
            for (BigInteger cp : coprimes) {
                BigInteger p = i.multiply(wheelBigInt).add(cp);
                if (p.compareTo(n) > 0) {
                    if (PrimeTester.modifiedMillerRabinTest(p, iterations, smallerPrimes))
                        primes.add(p);
                }
                if (primes.size() >= numPrimes) break;
            }
            if (primes.size() >= numPrimes) break;
        }
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, 0, primes, "N/A", "Primes");
    }


    public static Results wheelGenerator(BigInteger n) {
        return wheelGenerator(n, 30);
    }

    public static Results wheelGenerator(BigInteger n, int wheel) {
        return wheelGenerator(n, wheel, 16);
    }

    public static Results wheelGenerator(BigInteger n, int wheel, int iterations) {
        return wheelGenerator(n, wheel, iterations, 1000);
    }

    public static Results wheelGenerator(int numPrimes, BigInteger n) {
        return wheelGenerator(numPrimes, n, 30);
    }

    public static Results wheelGenerator(int numPrimes, BigInteger n, int wheel) {
        return wheelGenerator(numPrimes, n, wheel, 16);
    }

    public static Results wheelGenerator(int numPrimes, BigInteger n, int wheel, int iterations) {
        return wheelGenerator(numPrimes, n, wheel, iterations, 1000);
    }

    public static void main(String[] args) {
        /*System.gc();
        Results naiveResult = naivePrimeGenerator(BigInteger.valueOf(12000));
        System.out.println(naiveResult.toString(400));

        System.gc();
        Results sieveResult = sieveOfEratosthenes((long)(12000));
        System.out.println("Sieve of Eratosthenes:");
        System.out.print(sieveResult.toString(400));
        System.out.println("Largest Prime: " + ((ArrayList<Long>)sieveResult.getResult()).get(((ArrayList<Long>)sieveResult.getResult()).size()-1));

        System.gc();
        Results sundaramResult = sieveOfSundaram(12000);
        System.out.println("\nSieve of Sundaram:");
        System.out.print(sundaramResult.toString(400));
        System.out.println("Largest Prime: " + ((ArrayList<Long>)sundaramResult.getResult()).get(((ArrayList<Long>)sundaramResult.getResult()).size()-1));*/

        /*System.gc();
        Results largePrimes = wheelGenerator(BigIntegerUtils.randBigInteger(BigInteger.valueOf(2).pow(10), BigInteger.valueOf(2).pow(11)));
        System.out.println(largePrimes.toString(400));*/
        System.gc();
        //Results largeSieve = largeSieveOfEratosthenes(BigInteger.valueOf(Integer.MAX_VALUE/8));
        //System.out.println(largeSieve.toString(400));

        System.out.println(wheelGenerator(1, BigInteger.valueOf(1000000000), 2*3*5*7*11*13).toString(400));
        System.out.println(wheelGenerator(1, BigInteger.valueOf(1743035028), 2*3*5*7*11*13).toString(400));
    }
}
