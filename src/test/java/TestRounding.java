import java.math.BigDecimal;

import junit.framework.TestCase;
import norman.tools.Rounding;

public class TestRounding extends TestCase {

	double d0_1 = 0.011;
	double d0_2 = 0.015;
	double d0_3 = 0.016;
	double d0_4 = 0.025;
	
	public void testStandardRound(){
		// vielfache von 0,01
		assertEquals(new BigDecimal("0.01"), Rounding.Round(new BigDecimal("0.01"), ""));
		assertEquals(new BigDecimal("0.01"), Rounding.Round(new BigDecimal("0.011"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.015"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.016"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.025"), ""));
	}

	public void testStandardRound1(){
		// vielfache von 0,05
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.00"), "1"));
		assertEquals(new BigDecimal("0.05"), Rounding.Round(new BigDecimal("0.05"), "1"));
		assertEquals(new BigDecimal("0.10"), Rounding.Round(new BigDecimal("0.10"), "1"));
		
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.01"), "1"));
		assertEquals(new BigDecimal("0.05"), Rounding.Round(new BigDecimal("0.06"), "1"));
		assertEquals(new BigDecimal("0.10"), Rounding.Round(new BigDecimal("0.08"), "1"));

		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.025"), "1"));
		assertEquals(new BigDecimal("0.10"), Rounding.Round(new BigDecimal("0.075"), "1"));
	}

	public void testStandardRound2(){
		// vielfache von 0,10
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.02"), "2"));
		assertEquals(new BigDecimal("0.30"), Rounding.Round(new BigDecimal("0.3"), "2"));
		assertEquals(new BigDecimal("0.20"), Rounding.Round(new BigDecimal("0.25"), "2"));
		assertEquals(new BigDecimal("0.40"), Rounding.Round(new BigDecimal("0.35"), "2"));
		assertEquals(new BigDecimal("1.10"), Rounding.Round(new BigDecimal("1.07"), "2"));
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.05"), "2"));
	}

	public void testStandardRound3(){
		// vielfache von 0,50
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.00"), "3"));
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.25"), "3"));
		assertEquals(new BigDecimal("0.50"), Rounding.Round(new BigDecimal("0.26"), "3"));
		assertEquals(new BigDecimal("1.00"), Rounding.Round(new BigDecimal("0.75"), "3"));
		assertEquals(new BigDecimal("1.00"), Rounding.Round(new BigDecimal("1.25"), "3"));
		assertEquals(new BigDecimal("1.50"), Rounding.Round(new BigDecimal("1.5"), "3"));
		assertEquals(new BigDecimal("2.00"), Rounding.Round(new BigDecimal("1.75"), "3"));
	}
	
	public void testOddRound(){
		
		assertEquals(new BigDecimal("0.00"), Rounding.RoundCentOdd(new BigDecimal("0.00")));
		assertEquals(new BigDecimal("0.01"), Rounding.RoundCentOdd(new BigDecimal("0.01")));
		assertEquals(new BigDecimal("0.01"), Rounding.RoundCentOdd(new BigDecimal("0.014")));
		assertEquals(new BigDecimal("0.01"), Rounding.RoundCentOdd(new BigDecimal("0.015")));
		assertEquals(new BigDecimal("0.02"), Rounding.RoundCentOdd(new BigDecimal("0.0151")));
		assertEquals(new BigDecimal("0.03"), Rounding.RoundCentOdd(new BigDecimal("0.025")));
		assertEquals(new BigDecimal("-0.01"), Rounding.RoundCentOdd(new BigDecimal("-0.005")));
		assertEquals(new BigDecimal("0.01"), Rounding.RoundCentOdd(new BigDecimal("0.005")));
		
		BigDecimal l = 	new BigDecimal("1.50");
		BigDecimal k = 	new BigDecimal("-0.45");
		BigDecimal lk = Rounding.RoundCentOdd(l.multiply(k));
		assertEquals(new BigDecimal("-0.67"), lk);
	}
}
