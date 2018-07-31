package factoring;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import util.*;

import java.lang.Math;
import primes.PrimeGenerator;

public class Factoring {
	private BigInteger test;
	
	public Factoring(BigInteger a) {
		test = a;
	}





    public BigInteger pow(BigInteger x, BigInteger y) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(y)<0; i=i.add(BigInteger.ONE)) {
            x=x.multiply(x);
        }
        return x;
    }

    public BigDecimal log(BigInteger num) {
        BigDecimal digits = BigDecimal.ZERO;
        BigDecimal decnum = new BigDecimal(num);
        while (decnum.compareTo(BigDecimal.ONE)<0) {
            decnum=decnum.divide(new BigDecimal(10.0));
            digits=digits.add(BigDecimal.ONE);
        }
        double temp = num.doubleValue();
        return (digits.add(new BigDecimal(Math.log10(temp))));
    }

    public int[] matrixadd(int[][] matrix, int row1, int row2) {
	    int[] temp = new int [matrix[0].length];
	    for (int i=0; i<matrix[0].length; i++) {
	        temp[i]=matrix[row1][i]+matrix[row2][i];
        }
        for (int i=0; i<temp.length; i++) {
	        temp[i]%=2;
        }
        return temp;
    }

    /*
    public BigInteger order(BigInteger p, BigInteger b) {
	    if (p.gcd(b).compareTo(BigInteger.ONE)!=0) {
	        return BigInteger.valueOf(-1);
        }

        BigInteger k = BigInteger.valueOf(3);
	    boolean cont=true;
	    while (cont) {
	        if (b.modPow(k, p).compareTo(BigInteger.ONE)==0) {
	            cont=false;
	            k.subtract(BigInteger.ONE);
            }
            k=k.add(BigInteger.ONE);
        }
        return k;
    }
    */




    public Results naivePrimeGenerator(BigInteger limit) {
        if (limit.compareTo(BigInteger.valueOf(2))<0) {
            throw new IllegalArgumentException();
        }
        long startTime = System.nanoTime();
        long modCount = 0;
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        primes.add(BigInteger.valueOf(2));
        for (BigInteger i = BigInteger.valueOf(3); i.compareTo(limit) < 0; i = i.add(BigInteger.valueOf(2))) {
            boolean isPrime = true;
            for (BigInteger j : primes) {
                modCount++;
                if (i.mod(j).equals(BigInteger.ZERO)) {
                    isPrime = false; 
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            } 
        }
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, modCount, primes, "modulos", "Primes");
    }

	public Results sieveOfEratosthenes(long limit) {
        if (limit < 2) {
            throw new IllegalArgumentException();
        }
        long startTime = System.nanoTime();
        long operations = 0;
        int intLimit = (int) limit;
        boolean[] numberPrimality = new boolean[intLimit];
        for (int i = 2; i < numberPrimality.length; i++) {
            operations++;
            numberPrimality[i] = true;
        }
        for (int i = 2; i < (int) Math.pow(numberPrimality.length, .5) + 1; i++) {
            if (numberPrimality[i]) {
                for (int j = (int) Math.pow(i,2); j < numberPrimality.length; j += i) {
                    operations++;
                    numberPrimality[j] = false;
                }
            }
        }
        ArrayList<Long> primes = new ArrayList<Long>();
        for (int i = 2; i < numberPrimality.length; i++) {
            operations++;
            if (numberPrimality[i]) {
                primes.add((long)i);
            }
        }
        long endTime = System.nanoTime();
        return new Results(endTime - startTime, operations, primes, "array accesses", "Primes");
    }

    public ArrayList<BigInteger> wheel (int wheel) {
        //Generates all coprimes
        ArrayList<BigInteger> coprimes = new ArrayList<BigInteger>();
        for (BigInteger i = BigInteger.ONE; i.compareTo(test)<0; i=i.add(BigInteger.ONE)) {
            if (test.gcd(i).compareTo(BigInteger.ONE)==0) {
                coprimes.add(i);
            }
        }
        System.out.println(coprimes);
        //Generates primes
        ArrayList<BigInteger> primes = new ArrayList<BigInteger>();
        for (BigInteger i = test.divide(BigInteger.valueOf(wheel)); i.multiply(BigInteger.valueOf(wheel)).compareTo(test.multiply(BigInteger.TWO))<0; i=i.add(BigInteger.ONE)) {
            for (BigInteger coprime: coprimes) {
                BigInteger prime = i.multiply(BigInteger.valueOf(wheel)).add(coprime);
                if (prime.compareTo(test)>0 && millerRabinPrimalityTest(prime,8)) {
                    primes.add(prime);
                }
            }
        }
        return primes;
    }





    public boolean fermatPrimalityTest(int iterations) {
        if (test.compareTo(BigInteger.ONE)<=0) {
            return false;
        }
        BigInteger random;
        for (int i = 0; i < iterations; i++) {
            random = ((new BigDecimal(test.subtract(BigInteger.ONE))).multiply(new BigDecimal(Math.random()))).toBigInteger().add(BigInteger.ONE);
            if (!(random.modPow(test.subtract(BigInteger.ONE),test).compareTo(BigInteger.ONE)==0)) {
            	return false;
            }
        }
        return true;
    }
	
	public boolean millerRabinPrimalityTest(int iterations) {
        if (test.compareTo(BigInteger.ONE)<=0) {
            return false;
        }
        if (test.compareTo(BigInteger.valueOf(2))!=0 && test.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO)==0) {
            return false;
        }
        BigInteger s = test.subtract(BigInteger.ONE); 
        while (BigIntegerUtils.isEven(s)) {
            s = s.divide(BigInteger.valueOf(2));
        }
        BigInteger random, temp, mod;
        for (int i = 0; i < iterations; i++) {
            random = ((new BigDecimal(test.subtract(BigInteger.ONE))).multiply(new BigDecimal(Math.random()))).toBigInteger().add(BigInteger.ONE);
            temp = s;
            mod = random.modPow(temp,test);
            while (temp.compareTo(test.subtract(BigInteger.ONE))!=0 && mod.compareTo(test.subtract(BigInteger.ONE))!=0 && mod.compareTo(BigInteger.ONE)!=0) {
                mod = mod.multiply(mod).mod(test);
                temp = temp.multiply(BigInteger.valueOf(2));
            }
            if (mod.compareTo(test.subtract(BigInteger.ONE))!=0 && temp.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO)==0) {
                return false;
            }
        }
        return true;
    }

    public boolean millerRabinPrimalityTest(BigInteger n, int iterations) {
        if (n.compareTo(BigInteger.ONE)<=0) {
            return false;
        }
        if (n.compareTo(BigInteger.valueOf(2))!=0 && n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO)==0) {
            return false;
        }
        BigInteger s = n.subtract(BigInteger.ONE);
        while (BigIntegerUtils.isEven(s)) {
            s = s.divide(BigInteger.valueOf(2));
        }
        BigInteger random, temp, mod;
        for (int i = 0; i < iterations; i++) {
            random = ((new BigDecimal(n.subtract(BigInteger.ONE))).multiply(new BigDecimal(Math.random()))).toBigInteger().add(BigInteger.ONE);
            temp = s;
            mod = random.modPow(temp, n);
            while (temp.compareTo(n.subtract(BigInteger.ONE)) != 0 && mod.compareTo(n.subtract(BigInteger.ONE)) != 0 && mod.compareTo(BigInteger.ONE) != 0) {
                mod = mod.multiply(mod).mod(n);
                temp = temp.multiply(BigInteger.valueOf(2));
            }
            if (mod.compareTo(n.subtract(BigInteger.ONE)) != 0 && temp.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
                return false;
            }
        }
        return true;
    }

	
	
	
	
	public BigInteger PollardRho() {
		BigInteger x = ((new BigDecimal(test.subtract(BigInteger.ONE))).multiply(new BigDecimal(Math.random()))).toBigInteger().add(BigInteger.ONE);
		BigInteger c = ((new BigDecimal(test.subtract(BigInteger.ONE))).multiply(new BigDecimal(Math.random()))).toBigInteger().add(BigInteger.ONE);
		BigInteger y = (x.multiply(x).add(c)).mod(test);
		BigInteger temp = null;
		boolean cont = true;
		while (cont) {
			x=(x.multiply(x).add(c)).mod(test);
			y=(y.multiply(y).add(c)).mod(test);
			y=(y.multiply(y).add(c)).mod(test);
			temp = (x.subtract(y)).gcd(test);
			if (temp.compareTo(BigInteger.ONE)!=0 && temp.compareTo(test)!=0) {
				cont=false;
				return temp;
			}
		}
		return temp;
	}

    public ArrayList<BigInteger> Pollardp1(BigInteger B) {
        ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
        ArrayList<BigInteger> primes = (ArrayList<BigInteger>)PrimeGenerator.naivePrimeGenerator(B).getResult();
        BigInteger a = BigInteger.TWO;
        BigInteger e,f;
        for (int i=0; i<primes.size(); i++) {
            e = log(B).divide(log(primes.get(i)), BigDecimal.ROUND_HALF_UP).toBigInteger().add(BigInteger.ONE);
            f = pow(primes.get(i), e);
            a = a.modPow(f, test);
        }
        BigInteger g = (a.subtract(BigInteger.ONE)).gcd(test);
        if (g.compareTo(BigInteger.ONE)>0 && g.compareTo(test)<0) {
            factors.add(g);
            factors.add(test.divide(g));
        }
        return factors;
    }

    public ArrayList<BigInteger> Quadsieve(long A, long B) {
	    ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
	    BigInteger x = test.sqrt().add(BigInteger.ONE);
	    if ((x.multiply(x)).compareTo(test)<0) {
	        x.add(BigInteger.ONE);
        }
        ArrayList<Long> factorbase = PrimeGenerator.sieveOfEratosthenes(B).getResult();
	    ArrayList<BigInteger> tosieve = new ArrayList<BigInteger>();
	    for (int i=0; i<A; i++) {
	        BigInteger temp = x.add(BigInteger.valueOf(i));
	        temp=temp.multiply(temp).subtract(test);
	        tosieve.add(temp);
        }
	    System.out.println(tosieve);
        ArrayList<BigInteger> backup = new ArrayList<BigInteger>();
        for (int y=0; y<tosieve.size(); y++) {
            backup.add(tosieve.get(y));
        }
        if (tosieve.get(1).mod(BigInteger.TWO).compareTo(BigInteger.ZERO)==0) {
            for (int y=1; y<tosieve.size(); y+=2) {
                tosieve.set(y, tosieve.get(y).divide(BigInteger.TWO));
            }
        } else {
            for (int y=0; y<tosieve.size(); y+=2) {
                tosieve.set(y, tosieve.get(y).divide(BigInteger.TWO));
            }
        }
        for (int i=1; i<factorbase.size(); i++) {
            BigInteger root1, root2;
            BigInteger quadresidue = test.modPow(BigInteger.valueOf((factorbase.get(i)-1)/2), BigInteger.valueOf(factorbase.get(i)));
            System.out.println(quadresidue);
            if (quadresidue.compareTo(BigInteger.ONE)==0) {
                BigInteger temp = test.mod(BigInteger.valueOf(factorbase.get(i)));
                for (int j = 3; BigInteger.valueOf(j).compareTo(test)<0; j++) {
                    if (BigInteger.valueOf(j * j).mod(BigInteger.valueOf(factorbase.get(i))).compareTo(temp) == 0) { //someting wronfg with this
                        root1 = BigInteger.valueOf(j);
                        root2 = BigInteger.valueOf(factorbase.get(i)).subtract(root1);
                        root1 = root1.subtract(x).mod(BigInteger.valueOf(factorbase.get(i)));
                        root2 = root2.subtract(x).mod(BigInteger.valueOf(factorbase.get(i)));
                        System.out.println("roots " + root1 + " " + root2);
                        for (int b=root1.intValueExact(); b<tosieve.size(); b+=factorbase.get(i)) {
                            tosieve.set(b, tosieve.get(b).divide(BigInteger.valueOf(factorbase.get(i))));
                        }
                        for (int c=root2.intValueExact(); c<tosieve.size(); c+=factorbase.get(i)) {
                            tosieve.set(c, tosieve.get(c).divide(BigInteger.valueOf(factorbase.get(i))));
                        }
                        j=test.intValueExact();
                    }
                }
            } else {
                factorbase.remove(i);
                i--;
            }
        }
        System.out.println(tosieve);
        int[][] matrix = new int[factorbase.size()+1][factorbase.size()];
        int row=0;
        for (int i=0; i<tosieve.size(); i++) {
            if (tosieve.get(i).compareTo(BigInteger.ONE)==0) {
                BigInteger temp=backup.get(i);
                System.out.println(temp);
                for (int j=0; j<factorbase.size(); j++) {
                    if (temp.mod(BigInteger.valueOf(factorbase.get(j))).compareTo(BigInteger.ZERO)==0) {
                        matrix[row][j]++;
                        temp=temp.divide(BigInteger.valueOf(factorbase.get(j)));
                        j--;
                    }
                }
                row++;
            }
            if (row>=matrix.length) {
                i=tosieve.size();
                //break;
            }
        }
        for (int r=0; r<matrix.length; r++) {
            for (int c=0; c<matrix[0].length; c++) {
                matrix[r][c]%=2;
                System.out.print(matrix[r][c]);
            }
            System.out.println();
        }

        int lead=0;
        for (int r=0; r<matrix.length; r++) {
            boolean cont=true;
            if (matrix[0].length<=lead) {
                cont=false;
                break;
            }
            int i=r;
            while (matrix[i][lead]==0) {
                i++;
                if (matrix.length == i) {
                    i=r;
                    lead++;
                    if (matrix[0].length == lead) {
                        cont=false;
                        break;
                    }
                }
            }
            if (cont) {
                int[] temp = matrix[i];
                matrix[i] = matrix[r];
                matrix[r] = temp;
                System.out.println(r + " " + lead);
                int val = matrix[r][lead];
                for (int j = 0; j < matrix[0].length; j++) {
                    matrix[r][j] /= val;
                }
                for (int k = 0; k < matrix.length; k++) {
                    if (k == r) {
                        continue;
                    }
                    val = matrix[k][lead];
                    for (int j = 0; j < matrix[0].length; j++) {
                        matrix[k][j] -= (val * matrix[r][j]);
                    }
                }
                lead++;
            }
        }

        System.out.println();
        for (int r=0; r<matrix.length; r++) {
            for (int c=0; c<matrix[0].length; c++) {
                matrix[r][c]%=2;
                System.out.print(matrix[r][c]);
            }
            System.out.println();
        }
        factors.add(BigInteger.ONE);
        return factors;
    }





    public String extendedgcd(BigInteger y) {
		BigInteger x=test;
		if (y.compareTo(x)>0) {
			BigInteger swap=x;
			x=y;
			y=swap;
		}
		if (x.gcd(y).compareTo(BigInteger.ONE)!=0) {
			return "There is no inverse.";
		}
		ArrayList<String> snums1 = new ArrayList<String>();
		ArrayList<String> snums2 = new ArrayList<String>();
		ArrayList<String> remainder = new ArrayList<String>();
		ArrayList<BigInteger> nums3 = new ArrayList<BigInteger>();
		boolean cont=true;
		
		
		while (cont) {
			snums1.add(new String(""+x));
			snums2.add(new String(""+y));
			remainder.add(new String("" +x.divideAndRemainder(y)[1]));
			nums3.add(x.divideAndRemainder(y)[0]);
			if (x.divideAndRemainder(y)[1].compareTo(BigInteger.ZERO)==0) {
				cont=false;
				snums1.remove(snums1.size()-1);
				snums2.remove(snums2.size()-1);
				remainder.remove(remainder.size()-1);
				nums3.remove(nums3.size()-1);
				//System.out.println(x+" "+y);
			}
			BigInteger temp=x;
			x=y;
			y=temp.divideAndRemainder(y)[1];
		}
		
		String num1 = snums1.get(snums1.size()-1);
		String num2 = snums2.get(snums2.size()-1);
		BigInteger num1coefficient=BigInteger.ONE;
		BigInteger num2coefficient=nums3.get(nums3.size()-1);
		for (int i=snums1.size()-2; i>=0; i--) {	
			if (num1.compareTo(remainder.get(i))==0) { 
				if (num2.compareTo(snums2.get(i))==0) {
					num2coefficient=nums3.get(i).multiply(num1coefficient).add(num2coefficient);
					num1=snums1.get(i);
				}
			}
			else if (num2.compareTo(remainder.get(i))==0) {
				if (num1.compareTo(snums2.get(i))==0) {
					num1coefficient=nums3.get(i).multiply(num2coefficient).add(num1coefficient);
					num2=snums1.get(i);
				}
			}
		}
		return num1+"("+num1coefficient+")"+"-"+num2+"("+num2coefficient+")="+remainder.get(remainder.size()-1)+"\nThe inverse of "+num2+" is " +(new BigInteger(num1).subtract(num2coefficient)); 
	}






	public ArrayList<BigInteger> tofactor() {
		BigInteger temp = test;
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		ArrayList<Long> primes = (ArrayList<Long>) PrimeGenerator.sieveOfEratosthenes(1000).getResult();
		for (int i=0; i<primes.size(); i++) {
			if (test.mod(BigInteger.valueOf(primes.get(i))).compareTo(BigInteger.ZERO)==0) {
				test=test.divide(BigInteger.valueOf(primes.get(i)));
				factors.add(BigInteger.valueOf(primes.get(i)));
				i--;
			}
		}
		while (!(fermatPrimalityTest(8) && millerRabinPrimalityTest(8))) {
			if (test.compareTo(BigInteger.ONE)==0) {
				test=temp;
				return factors;
			}
			BigInteger factor = PollardRho();
			test=test.divide(factor);
			factors.add(factor);
		}
		if (fermatPrimalityTest(8) && millerRabinPrimalityTest(8)) {
			factors.add(test);
		}
		test=temp;
		return factors;
	}
	
	public static void main (String args[]) {
		System.out.println("Enter a number.");
		Scanner scan = new Scanner(System.in);
		Factoring num = new Factoring(scan.nextBigInteger());
		//System.out.println(num.Pollardp1(BigInteger.valueOf(1000)));
		System.out.println(num.tofactor());
		System.out.println(num.Quadsieve(10000, 100));
		//System.out.println(num.wheel(2*3*5*7*11*13));
		//System.out.println("Enter another number for extended gcd.");
		//System.out.println(num.extendedgcd(BigInteger.valueOf(scan.nextInt())));
		scan.close();
	}
}
