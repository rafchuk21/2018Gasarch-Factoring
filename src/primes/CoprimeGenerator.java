package primes;
import util.Results;

import java.math.BigInteger;
import java.util.ArrayList;

public class CoprimeGenerator {
    /**
     * Generates coprimes of n
     * @param n
     * @return
     */
    public static Results naiveCoprimeGenerator(BigInteger n) {
        long startTime = System.nanoTime();
        ArrayList<BigInteger> coprimes = new ArrayList<BigInteger>();
        for (BigInteger i = BigInteger.ONE; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
            if (n.gcd(i).compareTo(BigInteger.ONE) == 0) {
                coprimes.add(i);
            }
        }
        long endTime = System.nanoTime();

        return new Results(endTime - startTime, 0, coprimes, "N/A", "Coprimes of " + n.intValue());
    }
}
