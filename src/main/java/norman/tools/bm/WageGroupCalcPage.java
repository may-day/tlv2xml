package norman.tools.bm;

import java.math.BigDecimal;

import norman.tools.bm.document.PropertyMissingException;

public class WageGroupCalcPage  extends JBMDocumentPart{

	static final String[] knownFields = {
		"wgName",
		"wgTimeFactor",
		"wgCostPerHour",
		"wgOverheadCostFactor",
		"wgFactor1", 
		"wgFactor2",
	"wgPricePerHourHardCoded"};

	StringBuffer wgName, wgTimeFactor, wgCostPerHour, 
	wgOverheadCostFactor, wgFactor1, wgFactor2, wgPricePerHourHardCoded;

	public WageGroupCalcPage() {

		wgName = new StringBuffer();
		wgTimeFactor = new StringBuffer();
		wgCostPerHour = new StringBuffer();
		wgOverheadCostFactor = new StringBuffer();
		wgFactor1 = new StringBuffer();
		wgFactor2 = new StringBuffer();
		wgPricePerHourHardCoded= new StringBuffer();

		putProperty("wgName", new StringProperty(wgName, true, false));
		putProperty("wgTimeFactor", new StringProperty(wgTimeFactor, true, false));
		putProperty("wgCostPerHour", new StringProperty(wgCostPerHour , true, false));
		putProperty("wgOverheadCostFactor", new StringProperty(wgOverheadCostFactor, true, false));
		putProperty("wgFactor1", new StringProperty(wgFactor1, true, false));
		putProperty("wgFactor2", new StringProperty(wgFactor2, true, false));    	
		putProperty("wgPricePerHourHardCoded", new StringProperty(wgPricePerHourHardCoded, true, false));    	
	}

	public BigDecimal LohnGemeinkosten(){
		return dbl("wgOverheadCostFactor");
	}

	public BigDecimal Lohnkosten(){
		return dbl("wgCostPerHour");
	}

	public BigDecimal LohnFaktor(int faknumber){
		return dblFak("wgFactor"+faknumber);
	}

	public BigDecimal ZeitFaktor(){
		return dblFak("wgTimeFactor");
	}

	public BigDecimal StundenSatz(){
		String val=get("wgPricePerHourHardCoded");
		BigDecimal f;
		if (val == null || val.equals("")){
			f=Lohnkosten().multiply(LohnFaktor(1)).multiply(LohnFaktor(1));
		}else{
			f=dbl("wgPricePerHourHardCoded");
		}
		return f;
	}
}

class DefaultWageGroupCalcPage extends WageGroupCalcPage{

	public DefaultWageGroupCalcPage() throws PropertyMissingException {

	set("wgTimeFactor", "1.0");
	set("wgCostPerHour", "0.0");
	set("wgOverheadCostFactor", "0.0");
	set("wgFactor1", "1.0");
	set("wgFactor2", "1.0");
	}
}