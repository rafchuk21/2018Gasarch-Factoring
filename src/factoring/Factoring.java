package factoring;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
public class Factoring {
	private BigInteger test;
	
	public Factoring(BigInteger a) {
		test = a;
	}
	
	public ArrayList<BigInteger> gcdtesting() { 
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
		//System.out.println(xvalues);
		
		ArrayList<BigInteger> gcdt = new ArrayList<BigInteger>();
		//int index = -1;
		int index1 = 1;
		int index2 = 2;
		do {
			BigInteger testnum = xvalues.get(index2-1).subtract(xvalues.get(index1-1)).abs();
			BigInteger temp = test.gcd(testnum);
			if (temp.compareTo(BigInteger.ONE)!=0) {
				gcdt.add(temp);
			}
			index1++;
			index2=index1*2;
			//index++;
		} while (index2<xvalues.size() && gcdt.size()<1 /*&& gcdt.get(gcdt.size()-1).compareTo(BigInteger.ONE)==0*/);
		//System.out.println(gcdt);
		
		return gcdt;
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
	
	public static void main (String args[]) {
		System.out.println("Enter a number");
		Scanner scan = new Scanner(System.in);
		Factoring test1 = new Factoring(scan.nextBigInteger());
		System.out.println("Enter another number for extended gcd.");
		System.out.println(test1.extendedgcd(BigInteger.valueOf(scan.nextInt())));		
		scan.close();
	}
}
