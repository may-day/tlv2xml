package norman.tools.bm;

import java.math.BigDecimal;

import norman.tools.bm.document.PropertyMissingException;

public class OverheadGroupCalcPage  extends JBMDocumentPart{

	static final String[] knownFields = {
		"ogName"
		,"overheadCostAbsoluteOrFactor"
		,"overheadSurchargeFactor"
		,"overheadSharing_mark"
		,"calced_overhead_cost"
		,"calced_overhead_revenue"
	};


	StringBuffer ogName,overheadCostAbsoluteOrFactor,overheadSurchargeFactor,overheadSharing_mark,calced_overhead_cost,calced_overhead_revenue;

	public static final String Umschlag = "U";

	public OverheadGroupCalcPage() {

		ogName = new StringBuffer();
		overheadCostAbsoluteOrFactor = new StringBuffer();
		overheadSurchargeFactor = new StringBuffer();
		overheadSharing_mark = new StringBuffer();
		calced_overhead_cost = new StringBuffer();
		calced_overhead_revenue = new StringBuffer();

		putProperty("ogName", new StringProperty(ogName, true, false));
		putProperty("overheadCostAbsoluteOrFactor", new StringProperty(overheadCostAbsoluteOrFactor, true, false));
		putProperty("overheadSurchargeFactor", new StringProperty(overheadSurchargeFactor, true, false));
		putProperty("overheadSharing_mark", new StringProperty(overheadSharing_mark, true, false));
		putProperty("calced_overhead_cost", new StringProperty(calced_overhead_cost, true, false));
		putProperty("calced_overhead_revenue", new StringProperty(calced_overhead_revenue, true, false));

	}
	public String GemeinkostenName(){
		return get("ogName");
	}

	public BigDecimal KostenCentOderFaktor(){
		return dbl("overheadCostAbsoluteOrFactor");
	}

	public BigDecimal ZuschlagsFaktor(){
		return dbl("overheadSurchargeFactor");
	}

	public boolean isUmschlag(){
		return get("overheadSharing_mark").equals(Umschlag);
	}

	public BigDecimal KalkulierteKosten(){
		return dbl("calced_overhead_cost");
	}
	public BigDecimal KalkulierterUmsatz(){
		return dbl("calced_overhead_revenue");
	}

}

class DefaultOverhreadGroupCalcPage  extends OverheadGroupCalcPage{
	public DefaultOverhreadGroupCalcPage() throws PropertyMissingException{

		super();
		set("ogName", "");
		set("overheadCostAbsoluteOrFactor", "");
		set("overheadSurchargeFactor", "");
		set("overheadSharing_mark", "");
		set("calced_overhead_cost", "");
		set("calced_overhead_revenue", "");

	}
}