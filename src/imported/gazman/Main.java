package imported.gazman;

import imported.gazman.factor.Logger;
import imported.gazman.factor.QuadraticThieve;

import java.math.BigInteger;
import java.util.Random;

public class Main extends Logger {

    private static final Random random = new Random();

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {
        int length = 80;
        BigInteger a = BigInteger.valueOf(588437);
        BigInteger b = BigInteger.valueOf(602029);

        BigInteger input = a.multiply(b);
        //log(a, b);
        //log(input, input.toString().length());
        //log("---------");
        //log();
        QuadraticThieve qs = new QuadraticThieve(input);
        qs.start();
        while (qs.getResult().compareTo(BigInteger.ONE) == 0) {}
        System.out.println(qs.getResult());
    }
}
