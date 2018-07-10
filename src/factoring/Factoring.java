package factoring;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import util.*;
import primes.PrimeGenerator;

public class Factoring {
	private BigInteger test;
	
	public Factoring(BigInteger a) {
		test = a;
	}
	
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

	
	
	
	
	public BigInteger Pollard() {
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
		ArrayList<Long> primes = (ArrayList<Long>) PrimeGenerator.sieveOfEratosthenes(100).getResult();
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
			BigInteger factor = Pollard();
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
		System.out.println(num.tofactor());
		System.out.println("Enter another number for extended gcd.");
		System.out.println(num.extendedgcd(BigInteger.valueOf(scan.nextInt())));
		scan.close();
	}
}
