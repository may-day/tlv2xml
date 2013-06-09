package norman.tools.bm;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class CalcContext extends JBMDocumentPart
{
	static final String[] knownFields = {
		"calcpageNumber", 
		"dateCalcpageModify", "userCalcpageModify", 
		"calcPageName", "mgFact3isSurchargeDiscount",
		"BOQfinalFactor", 
		"discountAll", "discountMaterial", "discountWage", 
		"discountSpecial", 
		"factorAll", "factorMaterial", "factorWage", 
		"clientDiscount", 
		"gross", 
		"factorLimitMargin",
		"mode_surcharge_discount",
		"fixCostName1", "fixCost1", "calced_fixCost1_cost", "calced_fixCost1_revenue",
		"fixCostName2", "fixCost2", "calced_fixCost2_cost", "calced_fixCost2_revenue",
		"fixCostName3", "fixCost3", "calced_fixCost3_cost", "calced_fixCost3_revenue",
		"fixCostName4", "fixCost4", "calced_fixCost4_cost", "calced_fixCost4_revenue",
		"fixCostName5", "fixCost5", "calced_fixCost5_cost", "calced_fixCost5_revenue",
		"surchargeName1", "surchargeFactor1", "surchargeFactorProposal1", "calced_surcharge1_cost", "calced_surcharge1_revenue",
		"surchargeName2", "surchargeFactor2", "surchargeFactorProposal2", "calced_surcharge2_cost", "calced_surcharge2_revenue",
		"surchargeName3", "surchargeFactor3", "surchargeFactorProposal3", "calced_surcharge3_cost", "calced_surcharge3_revenue",
		"surchargeName4", "surchargeFactor4", "surchargeFactorProposal4", "calced_surcharge4_cost", "calced_surcharge4_revenue",
		"surchargeName5", "surchargeFactor5", "surchargeFactorProposal5", "calced_surcharge5_cost", "calced_surcharge5_revenue",
		"shippingName", "shippingAbsolute", "shippingFactor", "calced_shipping_cost", "calced_shipping_revenue",
		"handlingName", "handlingAbsolute", "handlingFactor", "calced_handling_cost", "calced_handling_revenue",
		"discountedName","calced_discounted_cost","calced_discounted_revenue",
		"boqName","calced_all_cost,calced_all_revenue",
		"calced_material_cost","calced_material_revenue",
		"calced_wage_cost","calced_wage_revenue"
	};


	StringBuffer calcpageNumber, 
	dateCalcpageModify, userCalcpageModify, 
	calcPageName, mgFact3isSurchargeDiscount,
	BOQfinalFactor, 
	discountAll, discountMaterial, discountWage, 
	discountSpecial, 
	factorAll, factorMaterial, factorWage, 
	clientDiscount, 
	gross, 
	factorLimitMargin,
	mode_surcharge_discount,
	fixCostName1, fixCost1, calced_fixCost1_cost, calced_fixCost1_revenue,
	fixCostName2, fixCost2, calced_fixCost2_cost, calced_fixCost2_revenue,
	fixCostName3, fixCost3, calced_fixCost3_cost, calced_fixCost3_revenue,
	fixCostName4, fixCost4, calced_fixCost4_cost, calced_fixCost4_revenue,
	fixCostName5, fixCost5, calced_fixCost5_cost, calced_fixCost5_revenue,
	surchargeName1, surchargeFactor1, surchargeFactorProposal1, calced_surcharge1_cost, calced_surcharge1_revenue,
	surchargeName2, surchargeFactor2, surchargeFactorProposal2, calced_surcharge2_cost, calced_surcharge2_revenue,
	surchargeName3, surchargeFactor3, surchargeFactorProposal3, calced_surcharge3_cost, calced_surcharge3_revenue,
	surchargeName4, surchargeFactor4, surchargeFactorProposal4, calced_surcharge4_cost, calced_surcharge4_revenue,
	surchargeName5, surchargeFactor5, surchargeFactorProposal5, calced_surcharge5_cost, calced_surcharge5_revenue,
	shippingName, shippingAbsolute, shippingFactor, calced_shipping_cost, calced_shipping_revenue,
	handlingName, handlingAbsolute, handlingFactor, calced_handling_cost, calced_handling_revenue,    
	discountedName,calced_discounted_cost,calced_discounted_revenue,
	boqName,calced_all_cost,calced_all_revenue,
	calced_material_cost,calced_material_revenue,
	calced_wage_cost,calced_wage_revenue;

	public HashMap<Integer, MaterialGroupCalcPage> mg = new HashMap<Integer, MaterialGroupCalcPage>();
	public HashMap<Integer, WageGroupCalcPage> lg = new HashMap<Integer, WageGroupCalcPage>();
	DefaultMaterialGroupCalcPage defaultMG; 
	DefaultWageGroupCalcPage defaultWG; 

	public CalcContext() throws DocumentException
	{
		super();
		calcpageNumber = new StringBuffer(); 
		dateCalcpageModify = new StringBuffer(); 
		userCalcpageModify = new StringBuffer(); 
		calcPageName = new StringBuffer();
		mgFact3isSurchargeDiscount = new StringBuffer();
		BOQfinalFactor = new StringBuffer(); 
		discountAll = new StringBuffer(); 
		discountMaterial = new StringBuffer(); 
		discountWage = new StringBuffer(); 
		discountSpecial = new StringBuffer(); 
		factorAll = new StringBuffer(); 
		factorMaterial = new StringBuffer(); 
		factorWage = new StringBuffer(); 
		clientDiscount = new StringBuffer(); 
		gross = new StringBuffer(); 
		factorLimitMargin = new StringBuffer();
		mode_surcharge_discount = new StringBuffer();
		fixCostName1 = new StringBuffer(); 
		fixCost1 = new StringBuffer();
		fixCostName2 = new StringBuffer(); 
		fixCost2 = new StringBuffer();
		fixCostName3 = new StringBuffer(); 
		fixCost3 = new StringBuffer();
		fixCostName4 = new StringBuffer(); 
		fixCost4 = new StringBuffer();
		fixCostName5 = new StringBuffer(); 
		fixCost5 = new StringBuffer();
		calced_fixCost1_cost = new StringBuffer();
		calced_fixCost1_revenue = new StringBuffer();		
		calced_fixCost2_cost = new StringBuffer();
		calced_fixCost2_revenue = new StringBuffer();		
		calced_fixCost3_cost = new StringBuffer();
		calced_fixCost3_revenue = new StringBuffer();		
		calced_fixCost4_cost = new StringBuffer();
		calced_fixCost4_revenue = new StringBuffer();		
		calced_fixCost5_cost = new StringBuffer();
		calced_fixCost5_revenue = new StringBuffer();
		calced_surcharge1_cost = new StringBuffer();
		calced_surcharge1_revenue = new StringBuffer();
		calced_surcharge2_cost = new StringBuffer();
		calced_surcharge2_revenue = new StringBuffer();
		calced_surcharge3_cost = new StringBuffer();
		calced_surcharge3_revenue = new StringBuffer();
		calced_surcharge4_cost = new StringBuffer();
		calced_surcharge4_revenue = new StringBuffer();
		calced_surcharge5_cost = new StringBuffer();
		calced_surcharge5_revenue = new StringBuffer();
		shippingName = new StringBuffer();
		calced_shipping_cost = new StringBuffer();
		calced_shipping_revenue = new StringBuffer();
		handlingName = new StringBuffer();
		calced_handling_cost = new StringBuffer();
		calced_handling_revenue = new StringBuffer();
		surchargeName1 = new StringBuffer(); 
		surchargeFactor1 = new StringBuffer(); 
		surchargeFactorProposal1 = new StringBuffer();
		surchargeName2 = new StringBuffer(); 
		surchargeFactor2 = new StringBuffer(); 
		surchargeFactorProposal2 = new StringBuffer();
		surchargeName3 = new StringBuffer(); 
		surchargeFactor3 = new StringBuffer(); 
		surchargeFactorProposal3 = new StringBuffer();
		surchargeName4 = new StringBuffer(); 
		surchargeFactor4 = new StringBuffer(); 
		surchargeFactorProposal4 = new StringBuffer();
		surchargeName5 = new StringBuffer(); 
		surchargeFactor5 = new StringBuffer(); 
		surchargeFactorProposal5 = new StringBuffer();
		shippingAbsolute = new StringBuffer(); 
		shippingFactor = new StringBuffer();
		handlingAbsolute = new StringBuffer(); 
		handlingFactor = new StringBuffer();
		discountedName = new StringBuffer();
		calced_discounted_cost = new StringBuffer();
		calced_discounted_revenue = new StringBuffer();
		boqName = new StringBuffer();
		calced_all_cost = new StringBuffer();
		calced_all_revenue = new StringBuffer();
		calced_material_cost = new StringBuffer();
		calced_material_revenue = new StringBuffer();
		calced_wage_cost = new StringBuffer();
		calced_wage_revenue = new StringBuffer();


		putProperty("calcpageNumber", new StringProperty(calcpageNumber, true, false));
		putProperty("dateCalcpageModify", new StringProperty(dateCalcpageModify, true, false));
		putProperty("userCalcpageModify", new StringProperty(userCalcpageModify, true, false));
		putProperty("calcPageName", new StringProperty(calcPageName, true, false));
		putProperty("mgFact3isSurchargeDiscount", new StringProperty(mgFact3isSurchargeDiscount, true, false));
		putProperty("BOQfinalFactor", new StringProperty(BOQfinalFactor, true, false));
		putProperty("discountAll", new StringProperty(discountAll, true, false));
		putProperty("discountMaterial", new StringProperty(discountMaterial, true, false));
		putProperty("discountWage", new StringProperty(discountWage, true, false));
		putProperty("discountSpecial", new StringProperty(discountSpecial, true, false));
		putProperty("factorAll", new StringProperty(factorAll, true, false));
		putProperty("factorMaterial", new StringProperty(factorMaterial, true, false));
		putProperty("factorWage", new StringProperty(factorWage, true, false));
		putProperty("clientDiscount", new StringProperty(clientDiscount, true, false));
		putProperty("gross", new StringProperty(gross, true, false));
		putProperty("factorLimitMargin", new StringProperty(factorLimitMargin, true, false));
		putProperty("mode_surcharge_discount", new StringProperty(mode_surcharge_discount, true, false));
		putProperty("fixCostName1", new StringProperty(fixCostName1, true, false));
		putProperty("fixCost1", new StringProperty(fixCost1, true, false));
		putProperty("fixCostName2", new StringProperty(fixCostName2, true, false));
		putProperty("fixCost2", new StringProperty(fixCost2, true, false));
		putProperty("fixCostName3", new StringProperty(fixCostName3, true, false));
		putProperty("fixCost3", new StringProperty(fixCost3, true, false));
		putProperty("fixCostName4", new StringProperty(fixCostName4, true, false));
		putProperty("fixCost4", new StringProperty(fixCost4, true, false));
		putProperty("fixCostName5", new StringProperty(fixCostName5, true, false));
		putProperty("fixCost5", new StringProperty(fixCost5, true, false));
		putProperty("calced_fixCost1_cost", new StringProperty(calced_fixCost1_cost,  true, false));
		putProperty("calced_fixCost1_revenue", new StringProperty(calced_fixCost1_revenue,  true, false));
		putProperty("calced_fixCost2_cost", new StringProperty(calced_fixCost2_cost,  true, false));
		putProperty("calced_fixCost2_revenue", new StringProperty(calced_fixCost2_revenue,  true, false));
		putProperty("calced_fixCost3_cost", new StringProperty(calced_fixCost3_cost,  true, false));
		putProperty("calced_fixCost3_revenue", new StringProperty(calced_fixCost3_revenue,  true, false));
		putProperty("calced_fixCost4_cost", new StringProperty(calced_fixCost4_cost,  true, false));
		putProperty("calced_fixCost4_revenue", new StringProperty(calced_fixCost4_revenue,  true, false));
		putProperty("calced_fixCost5_cost", new StringProperty(calced_fixCost5_cost,  true, false));
		putProperty("calced_fixCost5_revenue", new StringProperty(calced_fixCost5_revenue,  true, false));
		putProperty("calced_surcharge1_cost", new StringProperty(calced_surcharge1_cost,  true, false));
		putProperty("calced_surcharge1_revenue", new StringProperty(calced_surcharge1_revenue,  true, false));
		putProperty("calced_surcharge2_cost", new StringProperty(calced_surcharge2_cost,  true, false));
		putProperty("calced_surcharge2_revenue", new StringProperty(calced_surcharge2_revenue,  true, false));
		putProperty("calced_surcharge3_cost", new StringProperty(calced_surcharge3_cost,  true, false));
		putProperty("calced_surcharge3_revenue", new StringProperty(calced_surcharge3_revenue,  true, false));
		putProperty("calced_surcharge4_cost", new StringProperty(calced_surcharge4_cost,  true, false));
		putProperty("calced_surcharge4_revenue", new StringProperty(calced_surcharge4_revenue,  true, false));
		putProperty("calced_surcharge5_cost", new StringProperty(calced_surcharge5_cost,  true, false));
		putProperty("calced_surcharge5_revenue", new StringProperty(calced_surcharge5_revenue,  true, false));
		putProperty("surchargeName1", new StringProperty(surchargeName1, true, false));
		putProperty("surchargeFactor1", new StringProperty(surchargeFactor1, true, false));
		putProperty("surchargeFactorProposal1", new StringProperty(surchargeFactorProposal1, true, false));
		putProperty("surchargeName2", new StringProperty(surchargeName2, true, false));
		putProperty("surchargeFactor2", new StringProperty(surchargeFactor2, true, false));
		putProperty("surchargeFactorProposal2", new StringProperty(surchargeFactorProposal2, true, false));
		putProperty("surchargeName3", new StringProperty(surchargeName3, true, false));
		putProperty("surchargeFactor3", new StringProperty(surchargeFactor3, true, false));
		putProperty("surchargeFactorProposal3", new StringProperty(surchargeFactorProposal3, true, false));
		putProperty("surchargeName4", new StringProperty(surchargeName4, true, false));
		putProperty("surchargeFactor4", new StringProperty(surchargeFactor4, true, false));
		putProperty("surchargeFactorProposal4", new StringProperty(surchargeFactorProposal4, true, false));
		putProperty("surchargeName5", new StringProperty(surchargeName5, true, false));
		putProperty("surchargeFactor5", new StringProperty(surchargeFactor5, true, false));
		putProperty("surchargeFactorProposal5", new StringProperty(surchargeFactorProposal5, true, false));
		putProperty("shippingName", new StringProperty(shippingName,  true, false));
		putProperty("handlingName", new StringProperty(handlingName,  true, false));
		putProperty("calced_shipping_cost", new StringProperty(calced_shipping_cost,  true, false));
		putProperty("calced_shipping_revenue", new StringProperty(calced_shipping_revenue,  true, false));
		putProperty("calced_handling_cost", new StringProperty(calced_handling_cost,  true, false));
		putProperty("calced_handling_revenue", new StringProperty(calced_handling_revenue,  true, false));
		putProperty("shippingAbsolute", new StringProperty(shippingAbsolute, true, false));
		putProperty("shippingFactor", new StringProperty(shippingFactor, true, false));
		putProperty("handlingAbsolute", new StringProperty(handlingAbsolute, true, false));
		putProperty("handlingFactor", new StringProperty(handlingFactor, true, false));
		putProperty("discountedName", new StringProperty(discountedName, true, false));
		putProperty("calced_discounted_cost", new StringProperty(calced_discounted_cost, true, false));
		putProperty("calced_discounted_revenue", new StringProperty(calced_discounted_revenue, true, false));
		putProperty("boqName", new StringProperty(boqName, true, false));
		putProperty("calced_all_cost", new StringProperty(calced_all_cost, true, false));
		putProperty("calced_all_revenue", new StringProperty(calced_all_revenue, true, false));
		putProperty("calced_material_cost", new StringProperty(calced_material_cost, true, false));
		putProperty("calced_material_revenue", new StringProperty(calced_material_revenue, true, false));
		putProperty("calced_wage_cost", new StringProperty(calced_wage_cost, true, false));
		putProperty("calced_wage_revenue", new StringProperty(calced_wage_revenue, true, false));

		NumberFormat formatter = new DecimalFormat("00");
		for (int i=1; i < 20; i++){
			String id = formatter.format(i);
			mg.put(i, (MaterialGroupCalcPage)addPart ("mg"+id));
			lg.put(i, (WageGroupCalcPage)addPart ("wg"+id));
		}

		defaultMG = new DefaultMaterialGroupCalcPage(); 
		defaultWG = new DefaultWageGroupCalcPage();
		
		putProperty("unknown_1", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_2", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_3", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_4", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_5", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_6", new StringProperty(new StringBuffer(""), true, false));
		putProperty("unknown_7", new StringProperty(new StringBuffer(""), true, false));
		
	}    

	public JBMDocumentPart createDocumentPart (String kind) throws DocumentException{
		if (kind.startsWith("mg")){
			return new MaterialGroupCalcPage();
		}else if (kind.startsWith("wg")){
			return new WageGroupCalcPage();
		}
		return super.createDocumentPart(kind);
	}


	MaterialGroupCalcPage getMG(String materialGroup){
		MaterialGroupCalcPage g=null;
		try{
			g = mg.get(new Integer(materialGroup));
		}catch(NumberFormatException ex){}
			
		if (g == null)
			g = defaultMG;
		return g;
	}

	WageGroupCalcPage getLG(String wageGroup){
		WageGroupCalcPage g=null;
		try{
			g = lg.get(new Integer(wageGroup));
		}catch(NumberFormatException ex){}
		if (g == null)
			g = defaultWG;
		return g;
	}

	public BigDecimal EndFaktor(){
		return dblFak("BOQfinalFactor");
	}

	public BigDecimal GesamtFaktor(){
		return dblFak("factorAll");
	}

	public BigDecimal MaterialFaktor(){
		return dblFak("factorMaterial");

	}

	public boolean MaterialFaktor3AlsZuAbschlag(){
		String x = get("mgFact3isSurchargeDiscount");
		return x != null && x.equals("X");
	}

	public BigDecimal LohnFaktor(){
		return dblFak("factorWage");
	}

	public BigDecimal MaterialGemeinkosten(String materialGroup){
		return getMG(materialGroup).MaterialGemeinkosten();
	}

	public BigDecimal MaterialFaktor(String materialGroup, int faknumber){
		BigDecimal f;
		if (faknumber == 3 && MaterialFaktor3AlsZuAbschlag()){
			f=BigDecimal.ONE;
		}else{
			f=getMG(materialGroup).MaterialFaktor(faknumber);
		}
		return f;
	}

	public String Kalkulationsart(String materialGroup){
		return getMG(materialGroup).Kalkulationsart();    	
	}

	public boolean isListenpreiskalkulation(String materialGroup){
		return getMG(materialGroup).isListenpreiskalkulation();    	
	}

	public BigDecimal LohnKosten(String wageGroup){
		return getLG(wageGroup).Lohnkosten();
	}

	public BigDecimal LohnGemeinkosten(String wageGroup){
		return getLG(wageGroup).LohnGemeinkosten();
	}


	public BigDecimal ZeitFaktor(String wageGroup){
		return getLG(wageGroup).ZeitFaktor();
	}

	public BigDecimal LohnFaktor(String wageGroup, int faknumber){
		return getLG(wageGroup).LohnFaktor(faknumber);
	}

	public BigDecimal StundenSatz(String wageGroup){
		return getLG(wageGroup).StundenSatz();
		/*    	
    	if (enteredByHand){
    		STD-SATZ 
    	}else{
    		Lohnkosten je Stunde(LG) x FAKTOR1 x FAKTOR2

    	}
		 */

	}
}
