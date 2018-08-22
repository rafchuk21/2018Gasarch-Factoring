package imported.cuongvc;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class QuadraticSieveFactorization {

    /**
     * @param args
     */
    int maxPrime;
    int MAX_BLOCK = 1000000;
    int DELTA = 100;
    int nbPrimes;
    int[] primes;
    int[] S;
    int[] X;
    double[] L;
    double[] log;

    BigInteger N;
    BigInteger sqN;

    BigInteger p = BigInteger.valueOf(-1), q = BigInteger.valueOf(-1);

    ArrayList<Integer> key;
    ArrayList<Integer>[] list;
    HashMap<Integer, ArrayList<Integer>> a;

    BitSet[] f;

    public QuadraticSieveFactorization(int MAX_PRIME) {
        maxPrime = MAX_PRIME;
        sieve(maxPrime);
    }

    public QuadraticSieveFactorization() {

    }

    public void sieve(int MAX_PRIME) {
        int n = 0;
        int[] mark = new int[MAX_PRIME];
        for (int i = 2; i < MAX_PRIME; i++) {
            if (mark[i] == 0) {
                n++;
                for (int j = i + i; j < MAX_PRIME; j += i) {
                    mark[j] = 1;
                }
            }
        }
        primes = new int[n];
        for (int i = 0, j = 2; i < n; i++, j++) {
            while (mark[j] != 0) {
                j++;
            }
            primes[i] = j;
        }
        nbPrimes = n;
    }

    public BigInteger sqrt(BigInteger x) {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger y = x.divide(two);
        while (y.compareTo(x.divide(y)) > 0) {
            y = ((x.divide(y)).add(y)).divide(two);
        }
        return y;
    }

    public int power(int x, int n, int p) {
        long f = 1;
        for (long a = x; n > 0; n >>= 1) {
            if ((n & 1) == 1) {
                f = f * a % p;
            }
            a = a * a % p;
        }
        return (int) f;
    }

    public int TonelliShanks(int n, int p) {
        if (p == 2) {
            return (n == 1) ? 1 : -1;
        }
        if (power(n, (p - 1) / 2, p) != 1) {
            return -1;
        }
        int S = 0;
        int Q = p - 1;
        while ((Q & 1) == 0) {
            S++;
            Q >>= 1;
        }
        if (S == 1) {
            return power(n, (p + 1) / 4, p);
        }
        int z = -1;
        for (int i = 0; primes[i] < p; i++) {
            if (power(primes[i], (p - 1) / 2, p) == p - 1) {
                z = primes[i];
                break;
            }
        }
        if (z == -1) {
            return -1;
        }
        int c = power(z, Q, p);
        int R = power(n, (Q + 1) / 2, p);
        int t = power(n, Q, p);
        int M = S;
        while (t != 1) {
            long t2i = t;
            for (int i = 1; i < M; i++) {
                t2i = t2i * t2i % p;
                if (t2i == 1) {
                    int b = power(c, 1 << (M - i - 1), p);
                    R = (int) ((long) R * b % p);
                    c = (int) ((long) b * b % p);
                    t = (int) ((long) t * c % p);
                    M = i;
                    break;
                }
            }
        }
        return R;
    }

    public void init(BigInteger N) {
        this.N = N;
        sqN = sqrt(N);
        X = new int[nbPrimes];
        S = new int[nbPrimes];
        log = new double[nbPrimes];
        for (int i = 0; i < nbPrimes; i++) {
            log[i] = Math.log10(primes[i]);
            int n = (int) N.mod(BigInteger.valueOf(primes[i])).longValue();
            X[i] = TonelliShanks(n, primes[i]);
            S[i] = (int) sqN.mod(BigInteger.valueOf(primes[i])).longValue();
        }

        L = new double[MAX_BLOCK];
        list = new ArrayList[MAX_BLOCK];
        a = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < MAX_BLOCK; i++) {

            list[i] = new ArrayList<Integer>();
        }
        //BufferedWriter fo = new BufferedWriter(new FileWriter("abc.txt"));
        int r = 0;
        double esp = Math.log10(sqN.doubleValue());
        while (a.size() < nbPrimes + DELTA) {
            for (int i = 0; i < MAX_BLOCK; i++) {
                L[i] = 0;
                list[i].clear();
            }
            for (int i = 0; i < nbPrimes; i++) {
                int s;
                if (X[i] > 0) {
                    s = (X[i] - (S[i] + (r * MAX_BLOCK)) % primes[i] + primes[i]) % primes[i];
                    for (int j = s; j < MAX_BLOCK; j += primes[i]) {
                        L[j] += log[i];
                        list[j].add(i);
                    }
                    if (X[i] == 1 && primes[i] == 2) {
                        continue;
                    }
                    s = (primes[i] - X[i] - (S[i] + (r * MAX_BLOCK)) % primes[i] + primes[i]) % primes[i];
                    for (int j = s; j < MAX_BLOCK; j += primes[i]) {
                        L[j] += log[i];
                        list[j].add(i);
                    }
                }
            }

            for (int i = 0; i < MAX_BLOCK; i++) {
                if (L[i] < esp) {
                    continue;
                }
                BigInteger f = sqN.add(BigInteger.valueOf(i + r * MAX_BLOCK)).pow(2).subtract(N);
                ArrayList<Integer> u = new ArrayList<Integer>();
                ArrayList<Integer> v = new ArrayList<Integer>();
                for (int p : list[i]) {
                    int d = 0;
                    BigInteger P = BigInteger.valueOf(primes[p]);
                    while (f.mod(P).compareTo(BigInteger.ZERO) == 0) {
                        f = f.divide(P);
                        d++;
                    }
                    u.add(p);
                    v.add(d);
                }
                if (f.compareTo(BigInteger.ONE) == 0) {
                    a.put(i + r * MAX_BLOCK, new ArrayList<>(list[i]));
//					fo.write(a.size() + " " + (i + r * MAX_BLOCK) + "\n");
//					for (int j = 0; j < u.size(); j++) {
//						fo.write(primes[u.get(j)] + "(" + v.get(j) + ") ");
//					}
//					fo.write("\n");
                    if (a.size() == nbPrimes + DELTA) {
                        break;
                    }
                }
            }
            r++;
            //System.out.println(r + " " + a.size());
        }
        //fo.close();
        //System.out.println(primes.length + " " + a.size());
    }

    public void state1() {
        f = new BitSet[nbPrimes];
        for (int i = 0; i < f.length; i++) {
            f[i] = new BitSet();
        }
        key = new ArrayList<Integer>();
        for (int x : a.keySet()) {
            key.add(x);
        }
        for (int i = 0; i < key.size(); i++) {
            BigInteger n = sqN.add(BigInteger.valueOf(key.get(i))).pow(2).subtract(N);
            for (int p : a.get(key.get(i))) {
                int d = 0;
                BigInteger P = BigInteger.valueOf(primes[p]);
                while (n.mod(P).compareTo(BigInteger.ZERO) == 0) {
                    n = n.divide(P);
                    d ^= 1;
                }
                if (d == 1) {
                    f[p].set(i);
                }
            }
        }

        for (int i = f.length - 1; i > 0; i--) {
            int k = -1;
            for (int j = 0; j < a.size(); j++) {
                if (f[i].get(j)) {
                    k = j;
                    break;
                }
            }
            if (k > -1) {
                for (int j = i - 1; j >= 0; j--) {
                    if (f[j].get(k)) {
                        f[j].xor(f[i]);
                    }
                }
            }
        }
    }

    public void state2() {
        SecureRandom rand = new SecureRandom();
        BitSet[] F = new BitSet[nbPrimes];
        for (int i = 0; i < nbPrimes; i++) {
            F[i] = new BitSet();
            F[i].or(f[i]);
        }
        while (true) {
            int[] res = new int[a.size()];
            int[] val = new int[nbPrimes];
            for (int u = 0; u < f.length; u++) {
                ArrayList<Integer> x = new ArrayList<Integer>();
                for (int i = 0; i < a.size(); i++) {
                    if (f[u].get(i)) {
                        x.add(i);
                    }
                }

                if (x.size() > 0) {
                    for (int i = 1; i < x.size(); i++) {
                        int k = x.get(i);
                        res[k] = rand.nextInt(2);
                        for (int j = 0; j < f.length; j++) {
                            if (f[j].get(k)) {
                                f[j].set(k, false);
                                val[j] ^= res[k];
                            }
                        }
                    }
                    break;
                }
            }

            for (int i = 0; i < f.length; i++) {
                int k = -1;
                for (int j = 0; j < a.size(); j++) {
                    if (f[i].get(j)) {
                        k = j;
                        break;
                    }
                }
                if (k > -1) {
                    res[k] = val[i];
                    for (int j = i + 1; j < f.length; j++) {
                        if (f[j].get(k)) {
                            f[j].set(k, false);
                            val[j] ^= res[k];
                        }
                    }
                }
            }
            BigInteger A = BigInteger.ONE;
            BigInteger B = BigInteger.ONE;
            int[] count = new int[nbPrimes];
            for (int i = 0; i < a.size(); i++) {
                if (res[i] == 1) {
                    A = A.multiply(sqN.add(BigInteger.valueOf(key.get(i)))).mod(N);
                    BigInteger n = sqN.add(BigInteger.valueOf(key.get(i))).pow(2).subtract(N);
                    for (int p : a.get(key.get(i))) {
                        BigInteger P = BigInteger.valueOf(primes[p]);
                        while (n.mod(P).compareTo(BigInteger.ZERO) == 0) {
                            n = n.divide(P);
                            count[p]++;
                        }
                    }
                }
            }
            for (int i = 0; i < nbPrimes; i++) {
                if (count[i] == 0) {
                    continue;
                }
                count[i] >>= 1;
                B = B.multiply(BigInteger.valueOf(primes[i]).modPow(BigInteger.valueOf(count[i]), N)).mod(N);
            }

            A = A.add(N);
            BigInteger P = A.add(B).gcd(N);
            BigInteger Q = A.subtract(B).gcd(N);
            //System.out.println(P + " " + Q + " " + N);
            if (P.equals(BigInteger.ONE) || Q.equals(BigInteger.ONE)) {
                for (int i = 0; i < nbPrimes; i++) {
                    f[i].or(F[i]);
                }
            } else {
                p = P; q = Q;
                break;
            }
        }

    }

    public void Gauss() {
        //System.out.println("GAUSS");
        state1();
        state2();
    }

    public BigInteger[] solve(BigInteger N) {
        init(N);
        Gauss();
        BigInteger[] res = new BigInteger[2];
        res[0] = p; res[1] = q;
        return res;
    }

    public BigInteger[] solve (BigInteger N, boolean chooseBWithFormula) {
        maxPrime = (int) Math.exp(.5*Math.sqrt(N.bitLength() * BigInteger.valueOf(N.bitLength()).bitLength()));
        sieve(maxPrime);
        return solve(N);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        long start = System.nanoTime();
//		BigInteger P = BigInteger.probablePrime(75, new SecureRandom());
//		BigInteger Q = BigInteger.probablePrime(75, new SecureRandom());
//		BigInteger N = P.multiply(Q);
//		System.out.println(P + " " + Q + " " + N);
        BigInteger N = new BigInteger("767971435180333");
        new QuadraticSieveFactorization().solve(N, true);
        long finish = System.nanoTime();
        System.out.println("Run Time: " + ((finish - start) / 1000));
//		N = new BigInteger("984267056240922216882018531729796274586017073599701404786071");
//		new Factorization(500000).solve(N);
//		N = new BigInteger("691818389445070152360738813577007345128014365681917286952599");
//		new Factorization(500000).solve(N);
//		N = new BigInteger("1311322901042503507596142997221845657983261597982965944899425846329");
//		new Factorization(500000).solve(N);
//		long finish = System.currentTimeMillis();
//		System.out.println("Run Time: " + ((finish - start) / 1000));
    }

}