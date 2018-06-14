package primes;

import java.math.BigInteger;
import java.util.ArrayList;
import util.Results;

public class PrimeGenerator {
    /**
     * Generate primes from 2 to given limit using the naive method
     *
     * @param limit limit for generating primes
     * @return generated primes as an ArrayList of longs
     */
    public static Results naivePrimeGenerator(BigInteger limit) {
        if (limit.compareTo(BigInteger.TWO) < 0) {
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

    /**
     * Generate primes from 2 to given limit using the Sieve of Eratosthenes
     * If limit > 2^31, uses another method with a limit up to 2^62
     * @param limit limit for generating primes
     * @return generated primes as an ArrayList of longs
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

    public static void main(String[] args) {
        Results naiveResult = naivePrimeGenerator(BigInteger.valueOf(3));
        System.out.println(naiveResult.toString());

        Results sieveResult = sieveOfEratosthenes((long)(Integer.MAX_VALUE/64));
        System.out.println(sieveResult.toString(400));
        System.out.println("Largest Prime: " + ((ArrayList<Long>)sieveResult.getResult()).get(((ArrayList<Long>)sieveResult.getResult()).size()-1));
    }
}
