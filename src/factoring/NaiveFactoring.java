package factoring;
import util.Results;
import primes.PrimeGenerator;

import java.math.BigInteger;
import java.util.ArrayList;

public class NaiveFactoring {
    public static Results naiveFactor(BigInteger toFactor, BigInteger primeBound) {
        long startTime = System.nanoTime();
        ArrayList<Long> primes = (ArrayList<Long>) PrimeGenerator.sieveOfEratosthenes(primeBound.longValue()).getResult();
        ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
        int mods = 0;
        for (long prime : primes) {
            mods++;
            while (toFactor.mod(BigInteger.valueOf(prime)).compareTo(BigInteger.ZERO)==0) {
                factors.add(BigInteger.valueOf(prime));
                toFactor = toFactor.divide(BigInteger.valueOf(prime));
            }
            if (toFactor.compareTo(BigInteger.ONE)==0) {
                break;
            }
        }
        if (toFactor.compareTo(primeBound) < 0) { factors.add(toFactor); } else {factors.add(BigInteger.ONE);}
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, mods, factors, "modulos", "Factor");
    }

    public static Results naiveFactor(BigInteger toFactor) {
        return naiveFactor(toFactor, toFactor.sqrt().add(BigInteger.ONE));
    }

    public static void main(String[] args) {
        System.out.println(naiveFactor(BigInteger.valueOf(413158511).multiply(BigInteger.valueOf(413158511)), BigInteger.valueOf(400)));
    }
}
