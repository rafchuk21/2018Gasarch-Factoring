package test.factor;

import factoring.FactoringMethods;
import imported.gazman.factor.QuadraticThieve;
import primes.PrimeGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class FactoringTimer {
    public static void main(String[] args) {
        ArrayList<Integer> bitList = new ArrayList<Integer>();
        bitList.add(8); bitList.add(12); bitList.add(16); bitList.add(20); bitList.add(24); bitList.add(28); bitList.add(32);

        for (Integer i : bitList) {
            if (i < 36) {
                timeDiffSquares(i, 30, 15, 500);
            }
            timePollardRho(i, 30, 15, 500);
            if (i > 8)
                timeQuadraticSieve(i, 30, 15, 500);
        }
        System.exit(1);
    }

    private static ArrayList<TimeEntry> timeDiffSquares(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();
        BigInteger[] res;

        for (BigInteger n : numbers) {
            ConfidenceIntervalCalculator cicalc = new ConfidenceIntervalCalculator(2);
            while (cicalc.getSampleSize() < minSampleSize) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.diffSquareFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime);
                    //System.out.println(cicalc.getValues());
                }
            }

            cicalc.removeOutliers();

            while (cicalc.getSampleSize() < maxSampleSize && (cicalc.getMarginOfError() > .05 * cicalc.getMean() || cicalc.getSampleSize() < minSampleSize)) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.diffSquareFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime, true);
                }
            }

            cicalc.updateStatistics();

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Difference of Squares of " + n));
            System.out.println(times.size() + ":" + times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Jones/DiffSquares" + bits + "Bit.txt");
        }

        return times;
    }

    private static ArrayList<TimeEntry> timeDiffSquares(int bits, int numNumbers, int minSampleSize, int maxSampleSize) {
        ArrayList<BigInteger> numsToTest = new ArrayList<>();
        for (int i = 0; i < numNumbers; i++) {
            numsToTest.add(PrimeGenerator.RSAgen(bits));
        }

        return timeDiffSquares(numsToTest, minSampleSize, maxSampleSize, bits);
    }

    private static ArrayList<TimeEntry> timePollardRho(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();
        BigInteger[] res;

        for (BigInteger n : numbers) {
            ConfidenceIntervalCalculator cicalc = new ConfidenceIntervalCalculator(2);
            while (cicalc.getSampleSize() < minSampleSize) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.pollardRhoFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime);
                }
            }

            cicalc.removeOutliers();

            while (cicalc.getSampleSize() < maxSampleSize && (cicalc.getMarginOfError() > .05 * cicalc.getMean() || cicalc.getSampleSize() < minSampleSize)) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.pollardRhoFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime, true);
                }
            }

            cicalc.updateStatistics();

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Pollard Rho Method of " + n));
            System.out.println(times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Jones/PollardRho" + bits + "Bit.txt");
        }

        return times;
    }

    private static ArrayList<TimeEntry> timePollardRho(int bits, int numNumbers, int minSampleSize, int maxSampleSize) {
        ArrayList<BigInteger> numsToTest = new ArrayList<>();
        for (int i = 0; i < numNumbers; i++) {
            numsToTest.add(PrimeGenerator.RSAgen(bits));
        }

        return timePollardRho(numsToTest, minSampleSize, maxSampleSize, bits);
    }

    private static ArrayList<TimeEntry> timeQuadraticSieve(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();

        for (BigInteger n : numbers) {
            ConfidenceIntervalCalculator cicalc = new ConfidenceIntervalCalculator(2);
            for (int i = 0; i < minSampleSize; i++) {
                System.gc();
                startTime = System.nanoTime();
                QuadraticThieve qs = new QuadraticThieve(n);
                qs.start();
                while (qs.getResult().compareTo(BigInteger.ONE) == 0) { }
                endTime = System.nanoTime();
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
                cicalc.addTime(endTime - startTime);
            }

            cicalc.removeOutliers();

            while (cicalc.getMarginOfError() > cicalc.getMean() * .05 && cicalc.getSampleSize() < maxSampleSize) {
                System.gc();
                startTime = System.nanoTime();
                QuadraticThieve qs = new QuadraticThieve(n);
                qs.start();
                while (qs.getResult().compareTo(BigInteger.ONE) == 0) { }
                endTime = System.nanoTime();
                cicalc.addTime(endTime - startTime, true);
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
            }

            cicalc.updateStatistics();

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Quadratic Sieve of " + n));
            System.out.println(times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Jones/QuadraticSieve" + bits + "Bit.txt");
        }

        return times;
    }

    private static ArrayList<TimeEntry> timeQuadraticSieve(int bits, int numNumbers, int minSampleSize, int maxSampleSize) {
        ArrayList<BigInteger> numsToTest = new ArrayList<>();
        for (int i = 0; i < numNumbers; i++) {
            numsToTest.add(PrimeGenerator.RSAgen(bits));
        }

        return timeQuadraticSieve(numsToTest, minSampleSize, maxSampleSize, bits);
    }

    private static ArrayList<TimeEntry> timeQuadraticSieve(ArrayList<BigInteger> numbers) {
        return timeQuadraticSieve(numbers, 100, 1000, -1);
    }



    private static class TimeEntry {
        private int sampleSize;
        private long mean;
        private long marginOfError;
        private String descriptor;

        public TimeEntry(int sampleSize, long mean, long marginOfError, String descriptor) {
            this.sampleSize = sampleSize;
            this.mean = mean;
            this.marginOfError = marginOfError;
            this.descriptor = descriptor;
        }

        public TimeEntry (int sampleSize, long mean, long marginOfError) {
            this(sampleSize, mean, marginOfError, "");
        }

        public int getSampleSize() {return sampleSize;}
        public long getMean() {return mean;}
        public long getMarginOfError() {return marginOfError;}
        public String getDescriptor() {return descriptor;}

        public String toString() {
            return descriptor + ": " + mean + " +/-" + marginOfError + " n = " + sampleSize;
        }

        public void writeToFile(String path) {
            PrintWriter pw;
            try {
                pw = new PrintWriter(new FileOutputStream(new File(path), true));
                pw.println(descriptor.replaceAll("\\D+","") + ": " + mean + ", " + marginOfError + ", " + sampleSize);
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
