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
	"mgFactor3"};


	StringBuffer mgName, mgCalcListpriceBased, mgOverheadCostFactor, 
	mgFactor1, mgFactor2, mgFactor3;

	public static final String Listenpreiskalkulation = "L";

	public MaterialGroupCalcPage() {

		mgName = new StringBuffer();
		mgCalcListpriceBased = new StringBuffer();
		mgOverheadCostFactor = new StringBuffer();
		mgFactor1 = new StringBuffer();
		mgFactor2 = new StringBuffer();
		mgFactor3 = new StringBuffer();

		putProperty("mgName", new StringProperty(mgName, true, false));
		putProperty("mgCalcListpriceBased", new StringProperty(mgCalcListpriceBased, true, false));
		putProperty("mgOverheadCostFactor", new StringProperty(mgOverheadCostFactor , true, false));
		putProperty("mgFactor1", new StringProperty(mgFactor1, true, false));
		putProperty("mgFactor2", new StringProperty(mgFactor2, true, false));
		putProperty("mgFactor3", new StringProperty(mgFactor3, true, false));
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

		set("mgCalcListpriceBased", "L");
		set("mgOverheadCostFactor", "0.0");
		set("mgFactor1", "0,0");
		set("mgFactor2", "0,0");
		set("mgFactor3", "0,0");
	}
}