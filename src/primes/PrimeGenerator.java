package primes;

import java.math.BigInteger;
import java.util.ArrayList;

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
        //for every integer from 3 to the limit
        for (BigInteger i = BigInteger.valueOf(3); i.compareTo(limit) < 0; i = i.add(BigInteger.valueOf(1))) {
            boolean isPrime = true; //assume the number is prime for now
            //for every number already added to the primes list
            for (BigInteger j : primes) {
                modCount++;
                //if the number is divisible by the prime
                if (i.mod(j).equals(BigInteger.ZERO)) {
                    isPrime = false; //mark it as not prime
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            } //if the number never got marked as non-prime, add it to primes list
        }

        long endTime = System.nanoTime();
        return new Results(endTime - startTime, modCount, primes);
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
        //if the limit for generating primes is less than the max int value, change to ints
        if (limit < Integer.MAX_VALUE) {
            long startTime = System.nanoTime();
            long operations = 0;
            int intLimit = (int) limit;
            //array representing whether its index is prime or not
            boolean[] numberPrimality = new boolean[intLimit];
            for (int i = 2; i < numberPrimality.length; i++) {
                operations++;
                //assume every number 2 and up is prime
                numberPrimality[i] = true;
            }

            //for every integer from 2 to sqrt(max), if it's prime, cross out its multiples
            for (int i = 2; i < (int) Math.pow(numberPrimality.length, .5) + 1; i++) {
                if (numberPrimality[i]) {
                    for (int j = (int) Math.pow(i,2); j < numberPrimality.length; j += i) {
                        operations++;
                        numberPrimality[j] = false;
                    }
                }
            }

            //compile all the primes into a list
            ArrayList<Long> primes = new ArrayList<Long>();
            for (int i = 2; i < numberPrimality.length; i++) {
                operations++;
                if (numberPrimality[i]) {
                    primes.add((long)i);
                }
            }
            long endTime = System.nanoTime();
            return new Results(endTime - startTime, operations, primes);
        } else { //otherwise use other method for larger array sets
            return largeSieveOfEratosthenes(limit);
        }
    }

    public static Results largeSieveOfEratosthenes(long limit) {
        boolean[][] numberPrimality = new boolean[(int)(limit / (long) Integer.MAX_VALUE)][(int)Math.min(limit, (long) Integer.MAX_VALUE)];
        return null;
    }

    private static class Results {
        private long duration;
        private long operations;
        private Object result;

        public Results(long dur, long ops, Object r) {
            duration = dur;
            operations = ops;
            result = r;
        }

        public long getDuration() {
            return duration;
        }

        public long getOperations() {
            return operations;
        }

        public Object getResult() {
            return result;
        }
    }

    public static void main(String[] args) {
        Results naiveResult = naivePrimeGenerator(BigInteger.valueOf(1000000));
        System.out.println(naiveResult.getDuration() + " nanoseconds\n" + naiveResult.getOperations() +
                " modulos\nPrimes: " + naiveResult.getResult().toString().substring(0,200));

        Results sieveResult = sieveOfEratosthenes(1000000);
        System.out.println(sieveResult.getDuration() + " nanoseconds\n" + sieveResult.getOperations() +
            " array accesses\nPrimes: " + sieveResult.getResult().toString().substring(0,200));
    }
}
