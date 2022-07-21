import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import norman.tools.Rounding;

public class TestRounding{

	double d0_1 = 0.011;
	double d0_2 = 0.015;
	double d0_3 = 0.016;
	double d0_4 = 0.025;

	@Test
	public void testStandardRound(){
		// vielfache von 0,01
		assertEquals(new BigDecimal("0.01"), Rounding.Round(new BigDecimal("0.01"), ""));
		assertEquals(new BigDecimal("0.01"), Rounding.Round(new BigDecimal("0.011"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.015"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.016"), ""));
		assertEquals(new BigDecimal("0.02"), Rounding.Round(new BigDecimal("0.025"), ""));
	}

	@Test
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

	@Test
	public void testStandardRound2(){
		// vielfache von 0,10
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.02"), "2"));
		assertEquals(new BigDecimal("0.30"), Rounding.Round(new BigDecimal("0.3"), "2"));
		assertEquals(new BigDecimal("0.20"), Rounding.Round(new BigDecimal("0.25"), "2"));
		assertEquals(new BigDecimal("0.40"), Rounding.Round(new BigDecimal("0.35"), "2"));
		assertEquals(new BigDecimal("1.10"), Rounding.Round(new BigDecimal("1.07"), "2"));
		assertEquals(new BigDecimal("0.00"), Rounding.Round(new BigDecimal("0.05"), "2"));
	}

	@Test
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
	
	@Test
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

	
	public void emulateDelphi(){
		BigDecimal x1 = new BigDecimal(0.1f);
		BigDecimal x2 = new BigDecimal(0.01f);
		BigDecimal x3 = new BigDecimal(0.001f);
		BigDecimal x4 = new BigDecimal(0.0001f);
		BigDecimal y1 = new BigDecimal(5);
		BigDecimal y2 = new BigDecimal(50);
		BigDecimal y3 = new BigDecimal(500);
		BigDecimal y4 = new BigDecimal(5000);
		BigDecimal z1 = x1.multiply(y1);
		BigDecimal z2 = x2.multiply(y2);
		BigDecimal z3 = x3.multiply(y3);
		BigDecimal z4 = x4.multiply(y4);
		BigDecimal f = new BigDecimal(10);
		//MathContext mc = new MathContext(0, RoundingMode.HALF_EVEN);
		z1 = z1.setScale(0, RoundingMode.HALF_EVEN);
		z2 = z2.setScale(0, RoundingMode.HALF_EVEN);
		z3 = z3.setScale(0, RoundingMode.HALF_EVEN);
		z4 = z4.setScale(0, RoundingMode.HALF_EVEN);
		z1 = x1;
		z2 = x2;
		z3 = x3;
		z4 = x4.multiply(f);
		System.out.println(z1.toPlainString());
		System.out.println(z2.toPlainString());
		System.out.println(z3.toPlainString());
		System.out.println(x4.toPlainString());
		System.out.println(z4.toPlainString());
		double  d1 = 0.0001; 
		double  d2 = 10; 
		//double  d3 = 10;
		//float f1 = (float) (d1*d2);
		z4=new BigDecimal((float) (d1*d2*d2));
		
		System.out.println((new BigDecimal((float) (d1*d2))).toPlainString());
		System.out.println((z4).toPlainString());
		System.out.println((z4.multiply(y2)).setScale(0, RoundingMode.HALF_EVEN).toPlainString());
		System.out.println((z4.multiply(y3)).setScale(0, RoundingMode.HALF_EVEN).toPlainString());

		/*
		System.out.println(x1.toPlainString());
		System.out.println(x2.toPlainString());
		System.out.println(x3.toPlainString());
		System.out.println(x4.toPlainString());
		*/
	}
	@Test
	public void testCalc(){
		
		// faktor2: 999.88%, matfak: 8.34%, matvk: 50€, lvf:1.0
		assertEquals( "41,69", doMP(99988, 834, 5000, false, 1.0, 1.0f, 9.9988, 9.9988f, 0.0834, 0.0834f));
		assertEquals( "41,65", doMP(99988, 833, 5000, false, 1.0, 1.0f, 9.9988, 9.9988f, 0.0833, 0.0833f));
		assertEquals( "1,25", doMP(9960, 250, 5000, false, 1.0, 1.0f, 0.9960, 0.9960f, 0.025, 0.025f));

		// lvf: 10.0
		assertEquals( "35,16", doMP(7032, 1000, 5000, false, 10.0, 10.0f, 7.032, 7.032f, 0.1, 0.1f));
		
	}
	public String doMP(int n_faktor2, int n_matfak, int cent, boolean detail, double dbl_lvf, float flt_lvf, double dbl_faktor2, float flt_faktor2, double dbl_matfak, float flt_matfak){
		//int i = 834;
		
		double d_faktor2 = 0.0001;
		float faktor2 = (float)(d_faktor2 * n_faktor2);
//		double faktor2 = (d_faktor2 * n_faktor2);
		//faktor2 = 9
		if (detail){
			System.out.println("\nfaktor2 - rep:\n=================");
			System.out.println("used: faktor2:" + new BigDecimal(faktor2).toPlainString());
			System.out.println("dbl rep - faktor2:" + new BigDecimal(dbl_faktor2).toPlainString());
			System.out.println("flt rep - faktor2:" + new BigDecimal(flt_faktor2).toPlainString());
			System.out.println("d*n - faktor2:" + new BigDecimal(d_faktor2 * n_faktor2).toPlainString());
			System.out.println("(float)d*n - faktor2:" + new BigDecimal((float)(d_faktor2 * n_faktor2)).toPlainString());
		}
		double d_matfak = 0.0001;
		float matfak = (float)(d_matfak * n_matfak);
//		double matfak = (d_matfak * n_matfak);
		if (detail){
			System.out.println("\nmatfak - rep:\n=================");
			System.out.println("used: matfak:" + new BigDecimal(matfak).toPlainString());
			System.out.println("dbl rep - matfak:" + new BigDecimal(dbl_matfak).toPlainString());
			System.out.println("flt rep - matfak:" + new BigDecimal(flt_matfak).toPlainString());
			System.out.println("d*n - matfak:" + new BigDecimal(d_matfak * n_matfak).toPlainString());
			System.out.println("(float)d*n - matfak:" + new BigDecimal((float)(d_matfak * n_matfak)).toPlainString());
		}

//		double lvf = dbl_lvf;
		float lvf = (float)dbl_lvf;
		if (detail){
			System.out.println("\nlvf - rep:\n=================");
			System.out.println("used: lvf:" + new BigDecimal(lvf).toPlainString());
			System.out.println("dbl rep - lvf:" + new BigDecimal(dbl_lvf).toPlainString());
			System.out.println("flt rep - lvf:" + new BigDecimal(flt_lvf).toPlainString());
		}
		
		if (detail){
			System.out.println("\nfinalfak - rep:\n=================");
		}
		double d_faktor = matfak;
		if (detail) System.out.println("used matfak:" + new BigDecimal(d_faktor).toPlainString());
		if (detail) System.out.println("used faktor2:" + new BigDecimal(faktor2).toPlainString());
		if (detail) System.out.println("used lvf:" + new BigDecimal(lvf).toPlainString());
		
		d_faktor = d_faktor * faktor2 * lvf;
		if (detail) System.out.println("dbl finalfak:" + new BigDecimal(d_faktor).toPlainString());
		
//		float f_faktor = (float)d_faktor;
		double f_faktor = d_faktor;
		if (detail) System.out.println("used: finalfak:" + new BigDecimal(f_faktor).toPlainString());
		if (detail) System.out.println("dbl finalfak:" + new BigDecimal(d_faktor).toPlainString());
		if (detail) System.out.println("flt finalfak:" + new BigDecimal((float)d_faktor).toPlainString());
		
		String mp = new BigDecimal(cent * f_faktor).setScale(0, RoundingMode.HALF_EVEN).divide(new BigDecimal(100)).toPlainString();
		if (detail) System.out.println("\nresult in cents - rep:\n=================");
		if (detail) System.out.println("used: cents:" + new BigDecimal(cent * f_faktor).toPlainString());
		if (detail) System.out.println("used: ganze cents:" + new BigDecimal(cent * f_faktor).setScale(0, RoundingMode.HALF_EVEN).toPlainString());
		if (detail) System.out.println("in euro:" + new BigDecimal(cent * f_faktor).setScale(0, RoundingMode.HALF_EVEN).divide(new BigDecimal(100)).toPlainString());
		//float euro = cent/100.0f;
		return shortRep(mp);
	}

	String shortRep(float f){
		String erg;
		if(f == (long) f)
        	erg = String.format("%d",(long)f);
		else
			erg = String.format("%s", f);

		return shortRep(erg);
	}

	String shortRep(double d){
		String erg;
		if(d == (long) d)
        	erg = String.format("%d",(long)d);
		else
			erg = String.format("%s", d);

		return shortRep(erg);
	}
	
	String shortRep(String f){
		String erg = f;

		if (erg.contains(".")){
			erg = erg.replace('.',  ',');
		}
		if (erg.contains(",")){
			// trailing 0
			while (erg.length() > 0 && erg.endsWith("0")){
				erg = erg.substring(0,  erg.length()-1);
			}
		}
		// leading 0
		while (erg.length() > 0 && erg.startsWith("0")){
			erg = erg.substring(1,  erg.length());
		}

		// decimal point
		while (erg.length() > 0 && erg.endsWith(",")){
			erg = erg.substring(0,  erg.length()-1);
		}

		//
		if (erg.length() > 1 && erg.startsWith(",")){
			erg = "0"+erg;
		}
		return erg;
		
	}
	
}
