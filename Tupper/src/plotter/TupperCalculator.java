package plotter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TupperCalculator {
	
	BigInteger k;
	BigInteger y;
	BigInteger exp;
	String s;
	
	public TupperCalculator(String k){
		k="960939379918958884971672962127852754715004339660129306651505519271702802395266424689642842174350718121267153782770623355993237280874144307891325963941337723487857735749823926629715517173716995165232890538221612403238855866184013235585136048828693337902491454229288667081096184496091705183454067827731551705405381627380967602565625016981482083418783163849115590225610003652351370343874461848378737238198224849863465033159410054974700593138339226497249461751545728366702369745461014655997933798537483143786841806593422227898388722980000748404719";
		this.k = new BigInteger(k);
		s=k;
	}
	
	/*
	 * Executes formula at x, k + deltaY
	 */
	public boolean getPoint(int x, int deltaY){
		byte val[] = {(byte) deltaY};
		y = new BigInteger(val);
		y=y.add(k);
		exp = new BigInteger(y.toByteArray());
		
		exp = exp.mod(tbi(17));	//y % 17
		int intexp = exp.intValue();	//safe since 0 < y %17 < 17
		intexp = -17*x - intexp;
		y.divide(tbi(17));	//y/17
		BigDecimal result = (new BigDecimal(y,0)).setScale(intexp);
		
		return 0.5 < result.floatValue()%2 ;
		
		
	}
	
	public boolean getPoint2(int x, int deltaY){
		byte val[] = {(byte) deltaY};
		y = new BigInteger(val);
		y=y.add(k);
				
		
		double d = Double.parseDouble(s);
		BigInteger big = y.divide(tbi(17));
		y = y.mod(tbi(17));
		int  power =  -17*x - (y.intValue());
		BigDecimal dec = new BigDecimal(big);
		for(int count = 0; count < -power; count ++){
			dec = dec.divide(new BigDecimal(2));
		}
		
		return .5 < dec.divideAndRemainder(new BigDecimal(2))[1].doubleValue();
		
	}
	
	/*
	 * int to BigInteger
	 * endianness???
	 */
	public BigInteger tbi (int i){
		byte num[] = new byte[4];
		for(int j =0; j < 4; j++){
			num[3-j] = (byte)(i >>> (j * 8));
		}
		return new BigInteger(num);
	}
}
