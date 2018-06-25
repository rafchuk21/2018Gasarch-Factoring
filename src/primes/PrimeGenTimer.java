package primes;
import util.Results;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PrimeGenTimer {
    public static void main(String[] args) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("io/PrimeGenTimes.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Results r;
        for (int i = 2; i < 100000000; i+=10) {
            r = PrimeGenerator.sieveOfEratosthenes(i);
            pw.println(r.getOperations());
        }
        pw.close();
    }
}
