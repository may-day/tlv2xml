package norman.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rounding {

	static BigDecimal bd_konflikt_p = new BigDecimal("0.005");
	static BigDecimal bd_konflikt_n = new BigDecimal("-0.005");
	static BigDecimal bd0_01 = new BigDecimal("0.01");
	static BigDecimal bd0_05 = new BigDecimal("0.05");
	static BigDecimal bd0_10 = new BigDecimal("0.10");
	static BigDecimal bd0_50 = new BigDecimal("0.50");
	static BigDecimal bd1_00 = new BigDecimal("1.00");
	static BigDecimal bd5_00 = new BigDecimal("5.00");
	static BigDecimal bd10_00 = new BigDecimal("10.00");
	/*
	 * Auf 2 Stellen nach dem Komma runden. Im Konflikt auf gerade Ziffer runden
	 */

	public static BigDecimal Round(BigDecimal value, String kind){
		BigDecimal mul=bd0_01;
//		int roundingMethod = BigDecimal.ROUND_HALF_EVEN;
		//int roundingMethod = BigDecimal.ROUND_HALF_EVEN;
		RoundingMode roundingMethod = RoundingMode.HALF_EVEN;
		if (kind == null || kind.equals("")){mul = bd0_01;}
		else if (kind.equals("1")){mul = bd0_05;} 
		else if (kind.equals("2")){mul = bd0_10; } 
		else if (kind.equals("3")){mul = bd0_50;} 
		else if (kind.equals("4")){mul = bd1_00; } 
		else if (kind.equals("5")){mul = bd5_00;} 
		else if (kind.equals("6")){mul = bd10_00; } 
		return value.divide(mul, 0, roundingMethod).multiply(mul);
	}

	/*
	 * Auf ganze aufrunden.
	 */
	public static BigDecimal RoundEuroUp(BigDecimal value){
		return value.divide(bd1_00, 0, RoundingMode.HALF_UP).multiply(bd1_00);
	}

	/*
	 * Auf ganze Cent aufrunden.
	 */
	public static BigDecimal RoundCentUp(BigDecimal value){
		return value.divide(bd0_01, 0, RoundingMode.HALF_UP).multiply(bd0_01);
	}
	
	/*
	 * Auf ganze Cent runden, auf ungerade bei konflikt
	 */
	public static BigDecimal RoundCentOdd(BigDecimal value){
		BigDecimal even=value.divide(bd0_01, 0, RoundingMode.HALF_EVEN).multiply(bd0_01);
		BigDecimal diff = even.subtract(value);
		if (diff.compareTo(bd_konflikt_p) == 0){
			even = even.subtract(bd0_01);
		}else if(diff.compareTo(bd_konflikt_n) == 0){
			even = even.add(bd0_01);
		}
		return even;
	}
	
}