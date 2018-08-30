package test.factor;

import factoring.FactoringMethods;
import imported.gazman.factor.QuadraticThieve;
import imported.cuongvc.QuadraticSieveFactorization;
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
        /*for (int i = 8; i < 28; i++) {
            bitList.add(i);
        }*/
        /*for (int i = 34; i < 48; i += 2) {
            bitList.add(i);
        }*/

        /*for (int i = 48; i <= 64; i+= 4) {
            bitList.add(i);
        }*/
        for (int i = 60; i <= 72; i+=4) {
            bitList.add(i);
        }
        for (Integer i : bitList) {
            System.out.println(i);
            if (i < 34 && i > 32) {
                timeDiffSquares(i, 4, 25, 200);
            }
            if (i > 60)
                timePollardRho(i, 10, 1, 200);
            if (i > 8)
                timeCuongvcQuadraticSieve(i, 10, 1, 200);
        }

        /*QuadraticThieve qs = new QuadraticThieve(PrimeGenerator.RSAgen(64), 500000);
        System.out.println(qs.getResult());*/
        System.exit(1);
    }

    private static ArrayList<TimeEntry> timeDiffSquares(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();
        BigInteger[] res = new BigInteger[2];

        for (BigInteger n : numbers) {
            System.out.println(n);
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

            cicalc.updateStatistics();

            while (cicalc.getSampleSize() < maxSampleSize && (cicalc.getMarginOfError() > .05 * cicalc.getMean() || cicalc.getSampleSize() < minSampleSize)) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.diffSquareFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime, true);
                    cicalc.updateStatistics();
                }
            }

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Difference of Squares of " + n + ", Smallest Factor " + res[0].min(res[1])));
            System.out.println(times.size() + ":" + times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Home/DiffSquares" + bits + "Bit.txt");
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
        BigInteger[] res = new BigInteger[2];

        for (BigInteger n : numbers) {
            System.out.println(n);
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

            cicalc.updateStatistics();

            while (cicalc.getSampleSize() < maxSampleSize && (cicalc.getMarginOfError() > .05 * cicalc.getMean() || cicalc.getSampleSize() < minSampleSize)) {
                System.gc();
                startTime = System.nanoTime();
                res = FactoringMethods.pollardRhoFactoring(n);
                if (res[0].compareTo(n) < 0 && res[0].compareTo(BigInteger.ONE) > 0) {
                    endTime = System.nanoTime();
                    cicalc.addTime(endTime-startTime);
                }
                cicalc.updateStatistics();
            }

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Pollard Rho Method of " + n + ", Smallest Factor " + res[0].min(res[1])));
            System.out.println(times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Home/PollardRho" + bits + "Bit.txt");
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

    private static ArrayList<TimeEntry> timeGazmanQuadraticSieve(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();

        BigInteger[] res = new BigInteger[2];

        for (BigInteger n : numbers) {
            System.out.println(n);
            ConfidenceIntervalCalculator cicalc = new ConfidenceIntervalCalculator(2);
            for (int i = 0; i < minSampleSize; i++) {
                System.out.println(i);
                System.gc();
                startTime = System.nanoTime();
                QuadraticThieve qs = new QuadraticThieve(n);
                qs.start();
                while (qs.getResult().compareTo(BigInteger.ONE) == 0) { }
                res[0] = qs.getResult();
                endTime = System.nanoTime();
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
                cicalc.addTime(endTime - startTime);
            }

            cicalc.updateStatistics();

            while (cicalc.getMarginOfError() > cicalc.getMean() * .05 && cicalc.getSampleSize() < maxSampleSize) {
                System.gc();
                startTime = System.nanoTime();
                QuadraticThieve qs = new QuadraticThieve(n);
                qs.start();
                while (qs.getResult().compareTo(BigInteger.ONE) == 0) { }
                res[0] = qs.getResult();
                endTime = System.nanoTime();
                cicalc.addTime(endTime - startTime);
                cicalc.updateStatistics();
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
            }

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Quadratic Sieve of " + n + ", Smallest Factor " + res[0].min(n.divide(res[0]))));
            System.out.println(times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Home/QuadraticSieve" + bits + "Bit.txt");
        }

        return times;
    }

    private static ArrayList<TimeEntry> timeGazmanQuadraticSieve(int bits, int numNumbers, int minSampleSize, int maxSampleSize) {
        ArrayList<BigInteger> numsToTest = new ArrayList<>();
        for (int i = 0; i < numNumbers; i++) {
            numsToTest.add(PrimeGenerator.RSAgen(bits));
        }

        return timeGazmanQuadraticSieve(numsToTest, minSampleSize, maxSampleSize, bits);
    }

    public static ArrayList<TimeEntry> timeCuongvcQuadraticSieve(ArrayList<BigInteger> numbers, int minSampleSize, int maxSampleSize, int bits) {
        long startTime;
        long endTime;

        ArrayList<TimeEntry> times = new ArrayList<>();

        BigInteger[] res = new BigInteger[2];
        QuadraticSieveFactorization qs;

        for (BigInteger n : numbers) {
            System.out.println(n);
            ConfidenceIntervalCalculator cicalc = new ConfidenceIntervalCalculator(2);
            for (int i = 0; i < minSampleSize; i++) {
                System.out.println(i);
                System.gc();
                startTime = System.nanoTime();
                qs = new QuadraticSieveFactorization();
                res = qs.solve(n, true);
                endTime = System.nanoTime();
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
                cicalc.addTime(endTime - startTime);
            }

            cicalc.updateStatistics();

            while (cicalc.getMarginOfError() > cicalc.getMean() * .05 && cicalc.getSampleSize() < maxSampleSize) {
                System.gc();
                startTime = System.nanoTime();
                qs = new QuadraticSieveFactorization();
                res = qs.solve(n, true);
                endTime = System.nanoTime();
                cicalc.addTime(endTime - startTime);
                cicalc.updateStatistics();
                //System.out.println(cicalc.getSampleSize() + ", " + (endTime - startTime) + ", " + qs.getResult());
            }

            times.add(new TimeEntry(cicalc.getSampleSize(), cicalc.getMean(), cicalc.getMarginOfError(), "Quadratic Sieve of " + n + ", Smallest Factor " + res[0].min(n.divide(res[0]))));
            System.out.println(times.get(times.size()-1));
            times.get(times.size()-1).writeToFile("io/FactoringTimes/Home/QuadraticSieve" + bits + "Bit.txt");
        }

        return times;
    }

    private static ArrayList<TimeEntry> timeCuongvcQuadraticSieve(int bits, int numNumbers, int minSampleSize, int maxSampleSize) {
        ArrayList<BigInteger> numsToTest = new ArrayList<>();
        for (int i = 0; i < numNumbers; i++) {
            numsToTest.add(PrimeGenerator.RSAgen(bits));
        }

        return timeCuongvcQuadraticSieve(numsToTest, minSampleSize, maxSampleSize, bits);
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
                pw.println(descriptor.replaceAll("[^\\d,]","") + ", " + mean + ", " + marginOfError + ", " + sampleSize);
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
