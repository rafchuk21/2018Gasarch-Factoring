import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class Factoring {
	private BigInteger test;
	
	public Factoring(BigInteger a) {
		test = a;
	}
	
	public ArrayList<Integer> naive() {
		long startTime=System.nanoTime();
		int count=0;
		ArrayList<BigInteger> nums = new ArrayList<BigInteger>();
		nums.add(BigInteger.TWO); 
		for (BigInteger i = BigInteger.valueOf(3); i.compareTo(BigInteger.valueOf(1000000))<0; i=i.add(BigInteger.TWO)) {
			nums.add(i);
		}
			
		for (BigInteger i = BigInteger.ZERO; i.compareTo(BigInteger.valueOf(nums.size()))<0; i=i.add(BigInteger.ONE)) {
			for (BigInteger j = i.add(BigInteger.ONE); j.compareTo(BigInteger.valueOf(nums.size()))<0; j=j.add(BigInteger.ONE)) {
				if(nums.get(j.intValueExact()).mod(nums.get(i.intValueExact())).equals(BigInteger.ZERO)) {
					nums.remove(j.intValueExact());
					count++;
				}
			}
			System.out.println(i);
		}
		System.out.println(nums);
		long timeElapsed=System.nanoTime()-startTime;
		System.out.println("Mod was performed " + count + " times.");
		System.out.println("Took " + timeElapsed + " nanoseconds.");
		//1,000,000 mod-421502 and nanoseconds-497973274070 
				
		ArrayList<Integer> factors = new ArrayList<Integer>();
		for (BigInteger i = BigInteger.ZERO; nums.get(i.intValueExact()).compareTo(BigInteger.valueOf((int)Math.sqrt(test.doubleValue())))<0; i=i.add(BigInteger.ONE)) {
			if(test.mod(nums.get(i.intValueExact())).equals(BigInteger.ZERO)) {
				factors.add(nums.get(i.intValueExact()).intValueExact());
			}
		}
		return factors;
	}

	
	
	
	
	public ArrayList<BigInteger> gcdtesting() {
		long startTime=System.nanoTime();
		boolean cont = true; 
		BigInteger x = ((new BigDecimal(test)).multiply(new BigDecimal(Math.random()))).toBigInteger();
		//BigInteger x = BigInteger.valueOf((int)(test.doubleValue()*Math.random()));
		ArrayList<BigInteger> xvalues = new ArrayList<BigInteger>();
		xvalues.add(x);
		while (cont && xvalues.size()<500) {
			x=((x.multiply(x)).add(BigInteger.ONE)).mod(test);
			xvalues.add(x);
			for (int i=0; i<xvalues.size(); i++) {
				for (int j=i+1; j<xvalues.size(); j++) {
					if(xvalues.get(i).equals(xvalues.get(j))) {
						cont=false;
					}
				}
			}
		}
		System.out.println(xvalues);
		
		ArrayList<BigInteger> gcdt = new ArrayList<BigInteger>();
		int index = -1;
		int index1 = 1;
		int index2 = 2;
		do {
			BigInteger testnum = xvalues.get(index2-1).subtract(xvalues.get(index1-1)).abs();
			BigInteger temp = test.gcd(testnum);
			gcdt.add(temp);
			index1++;
			index2=index1*2;
			index++;
		} while (index2<xvalues.size() && gcdt.get(index).equals(BigInteger.ONE));
		System.out.println(gcdt);
		
		if (gcdt.get(gcdt.size()-1).compareTo(BigInteger.ONE)==0) {
			gcdtesting();
		}
		
		long timeElapsed=System.nanoTime()-startTime;
		System.out.println("Took " + timeElapsed + " nanoseconds.");
		return gcdt;
	}
	
	
	
	
	
	public String extendedgcd(BigInteger y) {
		BigInteger x=test;
		if (y.compareTo(x)>0) {
			BigInteger swap=x;
			x=y;
			y=swap;
		}
		if (x.mod(y).compareTo(BigInteger.ZERO)==0) {
			return "They are multiples of each other.";
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
		return num1+"("+num1coefficient+")"+"-"+num2+"("+num2coefficient+")="+remainder.get(remainder.size()-1); 
	}
	
}

//Runner
import java.math.BigInteger;
import java.util.Scanner;
public class PrimeFactors {	
	public static void main (String args[]) {
		System.out.println("Enter a number.");
		Scanner scan = new Scanner(System.in);
		Factoring test1 = new Factoring(scan.nextBigInteger());
		//System.out.println(test1.naive());
		test1.gcdtesting();
		System.out.println("Enter another number for extended gcd.");
		System.out.println(test1.extendedgcd(BigInteger.valueOf(scan.nextInt())));
		
		scan.close();
	}
}

