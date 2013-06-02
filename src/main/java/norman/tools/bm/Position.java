package norman.tools.bm;

import java.math.BigDecimal;
import java.util.HashMap;

import norman.tools.Rounding;


public class Position extends JBMDocumentPart
{
	public static final 
	String [] knownPosFields = {"rowheader", 
		"poskind", 
		"oz",
		"clientoz",
		"name",
		"goodsgroup",
		"ordernumber",
		"supplier",
		"suppliernumber",
		"manufacturer",
		"unit",
		"circa",
		"print_short_long",
		"formfeed",
		"marker",
		"directSellingPrice",
		"netpos",
		"rounding",
		"markZ",
		"surchargeFactor",
		"materialGroup",
		"materialPurchasePrice",
		"materialSupplierFactor",
		"materialPurchaseDiscount",
		"materialSellingPrice",
		"materialFactor",
		"wagePurchasePrice",
		"wageSellingPrice",
		"wageGroup",
		"wageGroup1",
		"wageGroup2",
		"wageGroup3",
		"wageGroup4",
		"wageGroup5",
		"wageMinutes1",
		"wageMinutes2",
		"wageMinutes3",
		"wageMinutes4",
		"wageMinutes5",
		"wageFactor",
		"amount",
		"ep",
		"gp",
		"cost",
		"revenue",
		"margin",
		"marginpercent",
		"hours",
		"calculatedmaterialprice",
		"calculatedwageprice",
	"shortText"};

	StringBuffer rowheader;
	StringBuffer poskind;
	StringBuffer oz;
	StringBuffer clientoz;
	StringBuffer name;
	StringBuffer goodsgroup;
	StringBuffer ordernumber;
	StringBuffer supplier;
	StringBuffer suppliernumber;
	StringBuffer manufacturer;
	StringBuffer unit;
	StringBuffer circa;
	StringBuffer print_short_long;
	StringBuffer formfeed;
	StringBuffer marker;
	StringBuffer directSellingPrice;
	StringBuffer netpos;
	StringBuffer rounding;
	StringBuffer markZ;
	StringBuffer surchargeFactor;
	StringBuffer materialGroup;
	StringBuffer materialPurchasePrice;
	StringBuffer materialSupplierFactor;
	StringBuffer materialPurchaseDiscount;
	StringBuffer materialSellingPrice;
	StringBuffer materialFactor;
	StringBuffer wagePurchasePrice;
	StringBuffer wageSellingPrice;
	StringBuffer wageGroup;
	StringBuffer wageGroup1;
	StringBuffer wageGroup2;
	StringBuffer wageGroup3;
	StringBuffer wageGroup4;
	StringBuffer wageGroup5;
	StringBuffer wageMinutes1;
	StringBuffer wageMinutes2;
	StringBuffer wageMinutes3;
	StringBuffer wageMinutes4;
	StringBuffer wageMinutes5;
	StringBuffer wageFactor;
	StringBuffer amount;
	StringBuffer ep;
	StringBuffer gp;
	StringBuffer cost;
	StringBuffer revenue;
	StringBuffer margin;
	StringBuffer marginpercent;
	StringBuffer hours;
	StringBuffer calculatedmaterialprice;
	StringBuffer calculatedwageprice;
	StringBuffer shortText;


	public Position ()
	{

		rowheader = new StringBuffer();
		poskind = new StringBuffer();
		oz = new StringBuffer();
		clientoz = new StringBuffer();
		name = new StringBuffer();
		goodsgroup = new StringBuffer();
		ordernumber = new StringBuffer();
		supplier = new StringBuffer();
		suppliernumber = new StringBuffer();
		manufacturer = new StringBuffer();
		unit = new StringBuffer();
		circa = new StringBuffer();
		print_short_long = new StringBuffer();
		formfeed = new StringBuffer();
		marker = new StringBuffer();
		directSellingPrice = new StringBuffer();
		netpos = new StringBuffer();
		rounding = new StringBuffer();
		markZ = new StringBuffer();
		surchargeFactor = new StringBuffer();
		materialGroup = new StringBuffer();
		materialPurchasePrice = new StringBuffer();
		materialSupplierFactor = new StringBuffer();
		materialPurchaseDiscount = new StringBuffer();
		materialSellingPrice = new StringBuffer();
		materialFactor = new StringBuffer();
		wagePurchasePrice = new StringBuffer();
		wageSellingPrice = new StringBuffer();
		wageGroup = new StringBuffer();
		wageGroup1 = new StringBuffer();
		wageGroup2 = new StringBuffer();
		wageGroup3 = new StringBuffer();
		wageGroup4 = new StringBuffer();
		wageGroup5 = new StringBuffer();
		wageMinutes1 = new StringBuffer();
		wageMinutes2 = new StringBuffer();
		wageMinutes3 = new StringBuffer();
		wageMinutes4 = new StringBuffer();
		wageMinutes5 = new StringBuffer();
		wageFactor = new StringBuffer();
		amount = new StringBuffer();
		ep = new StringBuffer();
		gp = new StringBuffer();
		cost = new StringBuffer();
		revenue = new StringBuffer();
		margin = new StringBuffer();
		marginpercent = new StringBuffer();
		hours = new StringBuffer();
		calculatedmaterialprice = new StringBuffer();
		calculatedwageprice = new StringBuffer();
		shortText = new StringBuffer();

		putProperty ("rowheader", new StringProperty (rowheader, true, false));
		putProperty ("poskind", new StringProperty (poskind, true, false));
		putProperty ("oz", new StringProperty (oz, true, false));
		putProperty ("clientoz", new StringProperty (clientoz, true, false));
		putProperty ("name", new StringProperty (name, true, false));
		putProperty ("goodsgroup", new StringProperty (goodsgroup, true, false));
		putProperty ("ordernumber", new StringProperty (ordernumber, true, false));
		putProperty ("supplier", new StringProperty (supplier, true, false));
		putProperty ("suppliernumber", new StringProperty (suppliernumber, true, false));
		putProperty ("manufacturer", new StringProperty (manufacturer, true, false));
		putProperty ("unit", new StringProperty (unit, true, false));
		putProperty ("circa", new StringProperty (circa, true, false));
		putProperty ("print_short_long", new StringProperty (print_short_long, true, false));
		putProperty ("formfeed", new StringProperty (formfeed, true, false));
		putProperty ("marker", new StringProperty (marker, true, false));
		putProperty ("directSellingPrice", new StringProperty (directSellingPrice, true, false));
		putProperty ("netpos", new StringProperty (netpos, true, false));
		putProperty ("rounding", new StringProperty (rounding, true, false));
		putProperty ("markZ", new StringProperty (markZ, true, false));
		putProperty ("surchargeFactor", new StringProperty (surchargeFactor, true, false));
		putProperty ("materialGroup", new StringProperty (materialGroup, true, false));
		putProperty ("materialPurchasePrice", new StringProperty (materialPurchasePrice, true, false));
		putProperty ("materialSupplierFactor", new StringProperty (materialSupplierFactor, true, false));
		putProperty ("materialPurchaseDiscount", new StringProperty (materialPurchaseDiscount, true, false));
		putProperty ("materialSellingPrice", new StringProperty (materialSellingPrice, true, false));
		putProperty ("materialFactor", new StringProperty (materialFactor, true, false));
		putProperty ("wagePurchasePrice", new StringProperty (wagePurchasePrice, true, false));
		putProperty ("wageSellingPrice", new StringProperty (wageSellingPrice, true, false));
		putProperty ("wageGroup", new StringProperty (wageGroup, true, false));
		putProperty ("wageGroup1", new StringProperty (wageGroup1, true, false));
		putProperty ("wageGroup2", new StringProperty (wageGroup2, true, false));
		putProperty ("wageGroup3", new StringProperty (wageGroup3, true, false));
		putProperty ("wageGroup4", new StringProperty (wageGroup4, true, false));
		putProperty ("wageGroup5", new StringProperty (wageGroup5, true, false));
		putProperty ("wageMinutes1", new StringProperty (wageMinutes1, true, false));
		putProperty ("wageMinutes2", new StringProperty (wageMinutes2, true, false));
		putProperty ("wageMinutes3", new StringProperty (wageMinutes3, true, false));
		putProperty ("wageMinutes4", new StringProperty (wageMinutes4, true, false));
		putProperty ("wageMinutes5", new StringProperty (wageMinutes5, true, false));
		putProperty ("wageFactor", new StringProperty (wageFactor, true, false));
		putProperty ("amount", new StringProperty (amount, true, false));
		putProperty ("ep", new StringProperty (ep, true, false));
		putProperty ("gp", new StringProperty (gp, true, false));
		putProperty ("cost", new StringProperty (cost, true, false));
		putProperty ("revenue", new StringProperty (revenue, true, false));
		putProperty ("margin", new StringProperty (margin, true, false));
		putProperty ("marginpercent", new StringProperty (marginpercent, true, false));
		putProperty ("hours", new StringProperty (hours, true, false));
		putProperty ("calculatedmaterialprice", new StringProperty (calculatedmaterialprice, true, false));
		putProperty ("calculatedwageprice", new StringProperty (calculatedwageprice, true, false));
		putProperty ("shortText", new StringProperty (shortText, true, false));

	}

	public String toString(){ return "[POSITION]"; }

	public String toXML()
	{
		String xml = "<position kenn=''{0}'' kenn=''{9}''>\n"+
		"  <oz>{1}</oz>\n"+
		"  <bez><![CDATA[{2}]]></bez>\n"+
		"  <kt1><![CDATA[{3}]]></kt1>\n"+
		"  <artnr>{4}</artnr>\n"+
		"  <wg>{5}</wg>\n"+
		"  <lief><![CDATA[{6}]]></lief>\n"+
		"  <lbnr><![CDATA[{7}]]></lbnr>\n"+
		"  <herst><![CDATA[{8}]]></herst>\n"+
		"</position>";
		//Object[] args = {kennung, oz, bez, kt1, artnr, wg, lief, lbnr, herst, ff};
		return xml; //MessageFormat.format (xml, args);
	}


	public HashMap<String, String> recalc(CalcContext cc){
		HashMap<String, String> erg= new HashMap<String, String>();
		
		erg.put("OZ", oz.toString());
		erg.put("(EKOSTEN)", EKosten(cc).toString());
		erg.put("(KOSTEN)", Kosten(cc).toString());
		erg.put("MatKosten", Materialkosten(cc).toString());
		erg.put("LohnKosten", Lohnkosten(cc).toString());
		erg.put("MATEK", MATEK().toString());
		erg.put("LFAK", LFAK().toString());
		erg.put("LRAB", LRAB().toString());
		erg.put("MATVK", dblCent("materialSellingPrice").toString());
		erg.put("MATFAK", MATFAK().toString());
		erg.put("LOHNEK", LOHNEK().toString());
		erg.put("LOHNFAK", LOHNFAK().toString());
		erg.put("(EP)", EPreis(cc).toString());
		erg.put("(MP)", Materialpreis(cc).toString());
		erg.put("(LP)", Lohnpreis(cc).toString());
		erg.put("(GP)", GesamtPreis(cc).toString());
		return erg;
	}

	/*************************************************************
	 * Kostenberechnung
	 * @param cc
	 * @return
	 ************************************************************/
	public BigDecimal EKosten(CalcContext cc){
		return Materialkosten(cc).add(Lohnkosten(cc));
	}

	public BigDecimal Kosten(CalcContext cc){
		return Rounding.RoundEuroUp(EKosten(cc).multiply(Menge())) ;
	}

	public BigDecimal Menge(){
		return dbl("amount");
	}

	public BigDecimal Materialkosten(CalcContext cc){

		BigDecimal MK=MATEK().multiply( LFAK()).multiply(BigDecimal.ONE.subtract(LRAB()));
		MK=MK.add( MK.multiply(MGK(cc)));
		return Rounding.RoundCentUp(MK);
	}

	public BigDecimal MATEK(){
		return dblCent("materialPurchasePrice");
	}

	public BigDecimal LFAK(){
		return dblFak("materialSupplierFactor");
	}

	public BigDecimal LRAB(){
		return dbl("materialPurchaseDiscount");
	}

	public BigDecimal MGK(CalcContext cc){
		String mg = get("materialGroup");
		return cc.MaterialGemeinkosten(mg);
	}

	public BigDecimal Lohnkosten(CalcContext cc){
		BigDecimal lgKosten1 = Rounding.RoundCentUp(Lohnstunden(1, cc).multiply(LohnkostenProStunde(1, cc)));
		BigDecimal lgKosten2 = Rounding.RoundCentUp(Lohnstunden(2, cc).multiply(LohnkostenProStunde(2, cc)));
		BigDecimal lgKosten3 = Rounding.RoundCentUp(Lohnstunden(3, cc).multiply(LohnkostenProStunde(3, cc)));
		BigDecimal lgKosten4 = Rounding.RoundCentUp(Lohnstunden(4, cc).multiply(LohnkostenProStunde(4, cc)));
		BigDecimal lgKosten5 = Rounding.RoundCentUp(Lohnstunden(5, cc).multiply(LohnkostenProStunde(5, cc)));

		BigDecimal l = 	LOHNEK();
		BigDecimal lohngemeinkosten = Rounding.RoundCentOdd(l.multiply(LGK(cc)));
		l = l.add(lohngemeinkosten);
		
		return l.add(lgKosten1).add(lgKosten2).add(lgKosten3).add(lgKosten4).add(lgKosten5);

	}

	public BigDecimal LOHNEK(){
		return dblCent("wagePurchasePrice");
	}

	public BigDecimal LGK(CalcContext cc){
		String lg = get("wageGroup");
		return cc.LohnGemeinkosten(lg);
	}

	public BigDecimal Lohnstunden(int wageGroupIdx, CalcContext cc){
		String wg = get("wageGroup"+wageGroupIdx);
		return LMIN(wageGroupIdx).multiply(cc.ZeitFaktor(wg)).divide(new BigDecimal("60.00"));
	}

	/*
	 * wageGroup in [1 .. 5]
	 */
	public BigDecimal LMIN(int wageGroup){
		return dbl("wageMinutes"+wageGroup);
	}

	public BigDecimal LohnkostenProStunde(int wageGroupIdx, CalcContext cc){
		String wg = get("wageGroup"+wageGroupIdx);
		return cc.LohnKosten(wg).multiply(BigDecimal.ONE.add(cc.LohnGemeinkosten(wg)));
	}

	/************************************************************************
	 * EPreisberechnung, Preis pro Einheit
	 * @param cc
	 * @return
	 */
	public BigDecimal EPreis(CalcContext cc){
		return Materialpreis(cc).add(Lohnpreis(cc));
	}

	public BigDecimal GesamtPreis(CalcContext cc){
		return Runden(EPreis(cc).multiply(Menge()));
	}
	
	public BigDecimal Materialpreis(CalcContext cc){
		
		BigDecimal fak = BigDecimal.ONE;
		if (!isDirektVK()){
			fak = KalkblattMaterialfaktor(1, cc) 
				.multiply(KalkblattMaterialfaktor(2, cc)) 
				.multiply(KalkblattMaterialfaktor(3, cc)) 
				.multiply(MATFAK())
				.multiply(LVMATFAK(cc)) 
				.multiply(LVGESAMTFAK(cc)); 
		}
		return Runden ( Rounding.RoundCentUp(Materialbasispreis(cc).multiply(fak)).multiply(cc.EndFaktor()) );
	}

	public BigDecimal Materialbasispreis(CalcContext cc){
		String mg = get("materialGroup");
		BigDecimal preis;
		if (cc.isListenpreiskalkulation(mg) || isDirektVK()){
			preis = dblCent("materialSellingPrice");
		}else{
			preis = Materialkosten(cc);
		}
		return preis;
	}

	/*
	 * @param faknummer [1,2,3]
	 */
	public BigDecimal KalkblattMaterialfaktor(int faknummer, CalcContext cc){
		String mg = get("materialGroup");
		return cc.MaterialFaktor(mg, faknummer);
	}

	public BigDecimal MATFAK(){
		if (isNettoPos() || isDirektVK())
			return new BigDecimal("1.00");
		return dblFak("materialFactor");
	}

	public BigDecimal LVMATFAK(CalcContext cc){
		if (isNettoPos() || isDirektVK())
			return new BigDecimal("1.00");
		return cc.MaterialFaktor();
	}

	public BigDecimal LVGESAMTFAK(CalcContext cc){
		if (isNettoPos() || isDirektVK())
			return new BigDecimal("1.00");
		return cc.GesamtFaktor();
	}


	public BigDecimal Lohnpreis(CalcContext cc){

		BigDecimal lp1 = Runden( Lohnstunden(1, cc).multiply(Stundensatz(1, cc)));
		BigDecimal lp2 = Runden( Lohnstunden(2, cc).multiply(Stundensatz(2, cc)));
		BigDecimal lp3 = Runden( Lohnstunden(3, cc).multiply(Stundensatz(3, cc)));
		BigDecimal lp4 = Runden( Lohnstunden(4, cc).multiply(Stundensatz(4, cc)));
		BigDecimal lp5 = Runden( Lohnstunden(5, cc).multiply(Stundensatz(5, cc)));

		BigDecimal fak = BigDecimal.ONE;
		
		if (!isDirektVK()){
			fak=KalkblattLohnfaktor(1, cc) 
				.multiply(KalkblattLohnfaktor(2, cc)) 
				.multiply(LOHNFAK())
				.multiply(LVLOHNFAK(cc)) 
				.multiply(LVGESAMTFAK(cc)); 
		} 
				
		BigDecimal lp = Runden ( Lohnbasispreis(cc).multiply(fak).multiply(cc.EndFaktor()) );

		return lp.add(lp1).add(lp2).add(lp3).add(lp4).add(lp5);
	}


	public BigDecimal Lohnbasispreis(CalcContext cc){
		BigDecimal preis;
		String wsp = get("wageSellingPrice");
		if (wsp == null || wsp.equals("") || !isDirektVK()){
			preis = Lohnkosten(cc);
		}else{
			preis = dblCent("wageSellingPrice");
		}
		return preis;
	}

	public BigDecimal KalkblattLohnfaktor(int faknummer, CalcContext cc){
		String mg = get("wageGroup");
		return cc.LohnFaktor(mg, faknummer);
	}

	public BigDecimal LOHNFAK(){
		if (isNettoPos() || isDirektVK())
			return new BigDecimal("1.00");
		return dblFak("wageFactor");
	}

	public BigDecimal LVLOHNFAK(CalcContext cc){
		if (isNettoPos() ||  isDirektVK())
			return new BigDecimal("1.00");
		return cc.LohnFaktor();
	}

	public BigDecimal Stundensatz(int wageGroupIdx, CalcContext cc){
		String wg = get("wageGroup"+wageGroupIdx);

		return cc.StundenSatz(wg)
		.multiply(LOHNFAK())
		.multiply(LVLOHNFAK(cc))
		.multiply(LVGESAMTFAK(cc))
		.multiply(cc.EndFaktor());
	}

	public boolean isNettoPos(){
		String nettopos = get("netpos");
		return nettopos.equals("N");
	}

	public boolean isDirektVK(){
		String direktvk= get("directSellingPrice");
		return direktvk.equals("D");
	}

	public BigDecimal Runden(BigDecimal val){
		String r = get("rounding");
		return Rounding.Round(val, r);
	}
}

