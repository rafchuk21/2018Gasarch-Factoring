package primes;
import util.Results;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import static primes.PrimeGenerator.primeProductCoprimeList;

public class PrimeGenTimer {
    public static void main(String[] args) {
        timePrimorialCoprimeGenerator();
    }

    public static void timePrimorialCoprimeGenerator() {
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> primesToTest = new ArrayList<Integer>();
        primesToTest.add(3); primesToTest.add(5); primesToTest.add(7); primesToTest.add(11);
        primesToTest.add(13); primesToTest.add(17); primesToTest.add(19);

        ArrayList<Integer> currentPrimeList = new ArrayList<Integer>();
        currentPrimeList.add(2); long primeProduct = 2;
        long startTime; long endTime;
        for (Integer p : primesToTest) {
            times = new ArrayList<>();
            currentPrimeList.add(p);
            primeProduct *= p;
            for (int i = 0; i < 500 || primeProduct == 6 && i < 1000; i++) {
                System.gc();
                startTime = System.nanoTime();
                primeProductCoprimeList(currentPrimeList);
                endTime = System.nanoTime();
                times.add(endTime-startTime);
            }
            times = filterOutliers(times);
            while (times.size() < 500 || marginOfError(times) > .05*meanTime(times) && times.size() <= 10000) {
                System.gc();
                startTime = System.nanoTime();
                primeProductCoprimeList(currentPrimeList);
                endTime = System.nanoTime();
                if (!isOutlier(times,endTime - startTime))
                    times.add(endTime-startTime);
            }
            System.out.println(primeProduct + "\t" + meanTime(times) + " +/- " + marginOfError(times) + " nanoseconds. " + times.size() + " trials.");
        }

    }

    public static ArrayList<Long> filterOutliers(ArrayList<Long> times) {
        Collections.sort(times);
        //System.out.println(times);
        Long med = times.get(times.size()/2);
        Long q1 = times.get(times.size()/4);
        Long q3 = times.get(times.size()/4*3);
        long iqr = q3-q1;

        long current = times.get(0);
        while (current < q1 - 2*iqr) {
            //System.out.println(current + ", " + iqr + ", " + q1);
            times.remove(0);
            current = times.get(0);
        }

        current = times.get(times.size()-1);
        while (current > q3 + 2*iqr) {
            //System.out.println(current + ", " + iqr + ", " + q3);
            times.remove(times.size() - 1);
            current = times.get(times.size()-1);
        }

        return times;
    }

    public static boolean isOutlier(ArrayList<Long> times, long value) {
        if (times.size() < 100) return false;
        Collections.sort(times);
        //System.out.println(times);
        Long med = times.get(times.size()/2);
        Long q1 = times.get(times.size()/4);
        Long q3 = times.get(times.size()/4*3);
        long iqr = q3-q1;

        return (value > q3 + 2*iqr || value < q1-2*iqr);
    }

    public static Long meanTime(ArrayList<Long> times) {
        long mean = 0;
        for (long t : times) {
            mean += t/times.size();
        }
        return mean;
    }

    public static long standardDeviation(ArrayList<Long> times) {
        long mean = meanTime(times);
        int n = times.size();
        long sd = 0;
        for (long t : times) {
            sd += Math.pow((t - mean),2)/n;
        }
        sd = (long) Math.pow(sd, .5);
        return sd;
    }

    /**
     * Calculates the margin of error of sample means for a 95% CI w/ a lot of data (>1000)
     * @return
     */
    public static long marginOfError(ArrayList<Long> times) {
        return (long) (standardDeviation(times)/Math.pow(times.size(), .5) * 1.96);
    }
}
