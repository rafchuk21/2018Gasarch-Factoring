package factoring;

import util.BigIntegerUtils;
import java.math.BigInteger;
import java.util.Arrays;

public class FactoringMethods {
    /**
     * Finds 2 factors of N using Fermat's Difference of Squares method
     * @param n
     * @return
     */
    public static BigInteger[] diffSquareFactoring(BigInteger n) {
        BigInteger[] factors = new BigInteger[2];
        if (n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
            factors[0] = BigInteger.valueOf(2);
            factors[1] = n.divide(BigInteger.valueOf(2));
            return factors;
        }
        BigInteger x = BigIntegerUtils.sqrt(n);
        if (x.multiply(x).compareTo(n) < 0) {x = x.add(BigInteger.ONE);}
        BigInteger t = x.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE);
        BigInteger r = x.multiply(x).subtract(n);
        while (r.compareTo(BigIntegerUtils.sqrt(r).pow(2)) != 0) { //while r is not square
            r = r.add(t);
            t = t.add(BigInteger.valueOf(2));
        }
        x = t.subtract(BigInteger.valueOf(1)).divide(BigInteger.valueOf(2));
        BigInteger y = BigIntegerUtils.sqrt(r);
        factors[0] = x.subtract(y);
        factors[1] = x.add(y);
        return factors;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(diffSquareFactoring(BigInteger.valueOf(23))));
    }
}
