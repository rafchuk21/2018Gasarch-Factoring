package primes;
import util.Results;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class PrimeGenTimer {
    public static void main(String[] args) {
        PrintWriter naive = null, eratosthenes = null, sundaram = null;
        try {
            naive = new PrintWriter(new File("io/PrimeGenTimes/naive.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            eratosthenes = new PrintWriter(new File("io/PrimeGenTimes/sieveOfEratosthenes.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            sundaram = new PrintWriter(new File("io/PrimeGenTimes/sieveOfSundaram.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int limit = 1000000;
        Results r;
        /*for (int i = 2; i < 10000; i+=10) {
            System.gc();
            r = PrimeGenerator.naivePrimeGenerator(i);
            naive.println(r.getDuration());
        }*/
        for (int i = 2; i < limit; i+=10) {
            System.gc();
            r = PrimeGenerator.sieveOfEratosthenes(i);
            eratosthenes.println(r.getDuration());
        }
        for (int i = 2; i < limit; i+=10) {
            System.gc();
            r = PrimeGenerator.sieveOfSundaram(i);
            sundaram.println(r.getDuration());
        }

        naive.close(); eratosthenes.close(); sundaram.close();
    }
}
