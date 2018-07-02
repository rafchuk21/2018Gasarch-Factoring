package primes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import util.Results;

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
        } else { // otherwise use other method for larger array sets
            return largeSieveOfEratosthenes(limit);
        }
    }

    public static Results largeSieveOfEratosthenes(long limit) {
        boolean[][] numberPrimality = new boolean[(int)(limit / (long) Integer.MAX_VALUE)][(int)Math.min(limit, (long) Integer.MAX_VALUE)];
        return null;
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

    public static void main(String[] args) {
        System.gc();
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
        System.out.println("Largest Prime: " + ((ArrayList<Long>)sundaramResult.getResult()).get(((ArrayList<Long>)sundaramResult.getResult()).size()-1));
    }
}
