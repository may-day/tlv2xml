package norman.tools.bm;

import java.math.BigDecimal;

import norman.tools.bm.document.PropertyMissingException;

public class MaterialGroupCalcPage  extends JBMDocumentPart{

	static final String[] knownFields = {
		"mgName",
		"mgCalcListpriceBased", 
		"mgOverheadCostFactor", 
		"mgFactor1", 
		"mgFactor2", 
		"mgFactor3",
		"calced_cost",
		"calced_revenue"
	};


	StringBuffer mgName, mgCalcListpriceBased, mgOverheadCostFactor, 
	mgFactor1, mgFactor2, mgFactor3, calced_cost, calced_revenue;

	public static final String Listenpreiskalkulation = "L";

	public MaterialGroupCalcPage() {

		mgName = new StringBuffer();
		mgCalcListpriceBased = new StringBuffer();
		mgOverheadCostFactor = new StringBuffer();
		mgFactor1 = new StringBuffer();
		mgFactor2 = new StringBuffer();
		mgFactor3 = new StringBuffer();
		calced_cost = new StringBuffer();
		calced_revenue = new StringBuffer();

		putProperty("mgName", new StringProperty(mgName, true, false));
		putProperty("mgCalcListpriceBased", new StringProperty(mgCalcListpriceBased, true, false));
		putProperty("mgOverheadCostFactor", new StringProperty(mgOverheadCostFactor , true, false));
		putProperty("mgFactor1", new StringProperty(mgFactor1, true, false));
		putProperty("mgFactor2", new StringProperty(mgFactor2, true, false));
		putProperty("mgFactor3", new StringProperty(mgFactor3, true, false));
		putProperty("calced_cost", new StringProperty(calced_cost, true, false));
		putProperty("calced_revenue", new StringProperty(calced_revenue, true, false));
		putProperty("unknown_1", new StringProperty(new StringBuffer(), true, false));
	}

	public BigDecimal MaterialGemeinkosten(){
		return dbl("mgOverheadCostFactor");
	}

	public BigDecimal MaterialFaktor(int faknumber){
		return dblFak("mgFactor"+faknumber);
	}

	public String Kalkulationsart(){
		return get("mgCalcListpriceBased");
	}

	public boolean isListenpreiskalkulation(){
		return get("mgCalcListpriceBased").equals(Listenpreiskalkulation);
	}
}

class DefaultMaterialGroupCalcPage  extends MaterialGroupCalcPage{
	public DefaultMaterialGroupCalcPage() throws PropertyMissingException{

		super();
		set("mgCalcListpriceBased", "L");
		set("mgOverheadCostFactor", "0.0");
		set("mgFactor1", "0,0");
		set("mgFactor2", "0,0");
		set("mgFactor3", "0,0");
	}
}