package norman.tools.bm.plugins.bm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import norman.tools.bm.DocumentException;
import norman.tools.bm.DocumentPartException;
import norman.tools.bm.JBMDocumentNames;
import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.document.PropertyMissingException;
import norman.tools.bm.plugins.FormatAdapterException;

class BaumanFormatVersion {
	static Pattern markeridx = Pattern.compile("\\D*(\\d*)");
	public String valid_for, valid_ql, valid_fieldname;
	public int valid_fieldindex;
	public HashMap<Pattern, String> qline_partname;
	public HashMap<Pattern, String[]> qline_linedef;
	public BaumanFormatVersion(BufferedReader def) throws IOException{
		qline_partname= new HashMap<Pattern, String>();
		qline_linedef= new HashMap<Pattern, String[]>();
		valid_fieldindex=0;
		String l;
		while ((l=def.readLine())!=null){
			if (l.startsWith("@VALID:")){
				String parts[] = l.split(":",2)[1].split(",");
				valid_for = parts[0];
				valid_ql = parts[1];
				valid_fieldname = parts[2];
			}
			else if (l.startsWith("!")){
				String parts[] = l.split(":",2);
				String partname = parts[0].substring(1);
				for(String ql: parts[1].split(",")){
					qline_partname.put(Pattern.compile(ql), partname);
				}
			}
			else if (l.startsWith("#")){
				String parts[] = l.split(",", 2);
				String ql = parts[0];
				if (parts.length>1){
					parts = parts[1].split(",");
					String what = ql.substring(1).toLowerCase().trim() + "_";
					int unknown=1;
					for(int i=0; i < parts.length; i++){
						if (parts[i].equals("?")) parts[i] = "unknown_" + what + unknown++;
					}
				}else{
					parts = new String[]{};
				}
				Pattern pat=Pattern.compile(ql);
				qline_linedef.put(pat, parts);
				if (pat.matcher(valid_ql).matches()){
					valid_fieldindex=1;
					for(String field:parts){
						if (field.equals(valid_fieldname)){
							break;
						}
						valid_fieldindex++;
					}
				}
			}
		}
	}
	String getPartname(String marker){
		for(Pattern pat:qline_partname.keySet()){
			if (pat.matcher(marker).matches()){
				String partname= qline_partname.get(pat);
				if (partname.contains("%1$")){
					Matcher m = markeridx.matcher(marker);
					if (m.matches()){
						String markernum=m.group(1);
						if (markernum.length()>0){
							Integer offset=0;
							// @@ urrrg, find better way - maybe best to encode offset in the DEF file?
							if (partname.startsWith("CALCPAGE.lg")) offset = 20;
							return String.format(partname, Integer.valueOf(markernum) - offset);
						}
					}
				}
				return partname;
			}
		}
		return null;
	}
	
	String [] getLinedef(String marker){
		
		for(Pattern pat:qline_linedef.keySet()){
			if (pat.matcher(marker).matches()){
				return qline_linedef.get(pat);
			}
		}
		return null;
	}
	
	int index(String[] fields, String fieldname){
		int idx = 0;
		for (String fname:fields){
			++idx;
			if (fname.equals(fieldname)) return idx;
		}
		return -1;
	}
}

class ParseContext {

	public int lineCounter;
	public int lastCounter, infiniLoop;
	
	public String pushbackLine;
	public DocumentPart lastDocPart;
	public int expectTextLines;
	public int expectShortTextLines;
	public String requestDocPart, requestField;
	public String lastOZ;
	boolean usePushbackLine;
	
	public BaumanFormatVersion bmfv;
	BufferedReader reader;
	BufferedWriter writer;
	public DocumentPart doc;
	public StringBuffer line;
	
	ParseContext(DocumentPart doc, BufferedReader reader, BufferedWriter writer){
		this.doc = doc;
		this.reader = reader;
		this.writer = writer;
		usePushbackLine = false;
		bmfv = null;
		lineCounter = 0;
		lastCounter = infiniLoop = 0;
		lastProcessedPosition (null, -1, -1);
		
	}
	public ParseContext(DocumentPart doc, BufferedReader reader){
		this(doc, reader, null);
	}
	
	public ParseContext(DocumentPart doc, BufferedWriter writer){
		this(doc, null, writer);
	}
	
	public void lastProcessedPosition (DocumentPart docPart, int countTextLinesAhead, int countShortTextLinesAhead)
	{
		lastDocPart = docPart;
		expectTextLines = countTextLinesAhead;
		expectShortTextLines = countShortTextLinesAhead;
	}

	public String nextLine () throws BMFormatException {
		String line;

		if (usePushbackLine) {
			usePushbackLine = false;
			line = pushbackLine;
		}
		else
		{
			try {
				line = reader.readLine();
				if (line != null)
					lineCounter++;
			} catch (IOException exc) {
				line = null;
			}
		}
		if (lastCounter == lineCounter) {
			infiniLoop++;
			if (infiniLoop > 10)
			{
				String[] parts = line.split(AbstractBaumanAdapter.FIELDSEPARATOR,-1);
				System.out.println(line+"\n");
				for (int i=0; i < parts.length; i++)
					System.out.println(i+".: " + parts[i]+"\n");
				throw new BMFormatException ("infiniloop at " + lineCounter);
			}
		}
		else {
			lastCounter = lineCounter;
			infiniLoop = 0;
		}
		//	System.out.println ("line >" + lineCounter + "<");
		return line;
	}

	public void pushBack (String line) {
		usePushbackLine = true;
		pushbackLine = line;
	}

	public void writeln (String line) throws IOException
	{
		//System.out.println (line);
		writer.write (line);
		//writer.flush();
		lineCounter++;
	}

}

public abstract class AbstractBaumanAdapter {
    static final String FIELDSEPARATOR = ";";
    static final char LINETYPEMARKER = '#';
    static final String LINESEPARATOR = "\r\n";

    /* Kopfdatenmarker */
    static final String k01 = "#K01";
    static final String[] k01fields = {"createdBy",
				       "bizarrePosCounter",
				       "unknown_k01_1",
				       "keepPosText",
				       "lockStructure",
				       "currency",
				       "unknown_k01_2","unknown_k01_3","unknown_k01_4",
				       "orderType",
				       "unknown_k01_5",
				       "clientType",
				       "netCalc",
				       "ozMask",
				       "labelTitle1",
				       "labelTitle2",
				       "labelTitle3",
				       "labelTitle4",
				       "salesPriceYearTable",
				       "unknown_k01_6","unknown_k01_7"};


    static final String k02 = "#K02";
    static final String[] k02fields = {"proposalID",
				       "unknown_k02_1","unknown_k02_2",
				       "proposalName1","proposalName2"};

    static final String k03 = "#K03";
    static final String[] k03fields = {"constructionSiteLine1", 
				       "constructionSiteLine2", 
				       "constructionSiteLine3"};

    static final String k04 = "#K04";
    static final String[] k04fields = {"clientid", 
				       "key1", 
				       "key2", 
				       "addressName1", 
				       "addressName2", 
				       "addressName3", 
				       "addressStreet", 
				       "addressCity", 
				       "addressCountry", 
				       "addressZipcode"};

    static final String k05 = "#K05";
    static final String[] k05fields = {"deliveryName1", 
				       "deliveryName2", 
				       "deliveryName3", 
				       "deliveryStreet", 
				       "deliveryCity", 
				       "deliveryCountry", 
				       "deliveryZipcode", 
				       "MrMs", 
				       "unknown_k05_1", 
				       "unknown_k05_2"};

    static final String k06 = "#K06";
    static final String[] k06fields = {"clientRequest", 
				       "clientOrderId", 
				       "deliveryConditions", 
				       "shippingType"};

    static final String k07 = "#K07";
    static final String[] k07fields = {"warranty", 
				       "proposalFixationTimeLimit", 
				       "deliveryDate", 
				       "deliveryDateText", 
				       "processor",
				       "signature1", 
				       "signature2", 
				       "printoutType", 
				       "printoutSettings"};

    static final String[] printoutSettings = {"printAs", "shortPrint", "letterHeader", "address", "repeatLetterHeader",
					      "repeatAddress", "repeatProject", "repeatConstructionSite",
					      "posShortText", "posLongText", "posName", "posOrderNumber", "posMakeType", 
					      "posSubPrice", "posTechnicalData", "otherNullPos", "unknown_po_1",
					      "unitPrices", "posUnitPrice", "addons", "otherPrintNet", "unknown_po_2",
					      "otherNullTitle", "unknown_po_3", "titelsums", "unknown_po_4", "unknown_po_5",
					      "otherAddCarry", "deliveryAdress", "termsAndConditions", "headlineNumber",
					      "addressRight", "standardTexts", "proposalSubmittalLetterT1",
					      "startTextT2", "startTextT3", "endTextTE",
					      "posEmptyLineBeforeMakeType", "posEmptyLineBeforePrice", "posShortTextBold",
					      "posLongTextFirstLineBold", "posNameAsType", "posOrderNumberAsType",
					      "posMakeTypeOrSimilar"};

    static final String k08 = "#K08";
    static final String[] k08fields = {"paymentMode", 
					"VATtext", 
					"VATidx", 
					"VATproz", 
					"paymentConditionText", 
					"paymentConditionDays1", 
					"paymentConditionDays2",
					"paymentConditionPercent", 
					"paymentConditionDaysNetText", 
					"paymentConditionDaysNet"};

    static final String k09 = "#K09";
    static final String[] k09fields = {"kindText", 
				       "object_gewerk", 
				       "chances", 
				       "unknown_k09_1", 
				       "projectManager", 
				       "lumpOrder", 
				       "desiredDate", 
				       "contractNumber", 
				       "unknown_k09_2", 
				       "officecode", 
				       "unknown_k09_3", "unknown_k09_4", 
				       "unknown_k09_5", 
				       "officecode/sellercode", 
				       "unknown_k09_6", "unknown_k09_7", 
				       "unknown_k09_8", "unknown_k09_9", 
				       "unknown_k09_10", "unknown_k09_11", 
				       "unknown_k09_12", "unknown_k09_13", 
				       "unknown_k09_14", "unknown_k09_15", 
				       "unknown_k09_16", "unknown_k09_17", 
				       "unknown_k09_18", "unknown_k09_19", 
				       "unknown_k09_20", "unknown_k09_21", 
				       "unknown_k09_22", "unknown_k09_23", 
				       "unknown_k09_24"};

    static final String k10 = "#K10";
    static final String[] k10fields = {"dateBOQCreation", "userBOQCreation", 
				       "dateBOQChange", "userBOQChange", 
				       "dateProposalSubmittalLetter", "userProposalSubmittalLetter", 
				       "dateOrderConfirmation", "userOrderConfirmation", 
				       "dateBOQLock", "userBOQLock", 
				       "datePLReadIn", "userPLReadIn", 
				       "dateGAEBTransmission", "userGAEBTransmission", 
				       "dateCostRecalc", "userCostRecalc", 
				       "dateInvoice", "unknown_k10_1", 
				       "unknown_k10_2", "unknown_k10_3"};

    static final String k11 = "#K11";
    static final String[] k11fields = {};

    static final String k12 = "#K12";
    static final String[] k12fields = {};

    /* Kalkblatt diverse Faktoren, Zu-und Abschläge*/
    static final String a1 = "#A1 ";
    static final String[] a1fields = {"calcpageNumber", 
				      "dateCalcpageModify", "userCalcpageModify", 
				      "calcPageName", 
				      "mgFact3isSurchargeDiscount", 
				      "BOQfinalFactor", 
				      "discountAll", "discountMaterial", "discountWage", 
				      "discountSpecial", 
				      "factorAll", "factorMaterial", "factorWage", 
				      "clientDiscount", 
				      "gross", 
				      "factorLimitMargin"};

    static final String a2 = "#A2 ";
    static final String[] a2fields = {"fixCostName1", "fixCost1",
				      "fixCostName2", "fixCost2",
				      "fixCostName3", "fixCost3",
				      "fixCostName4", "fixCost4",
				      "fixCostName5", "fixCost5"};

    static final String a3 = "#A3 ";
    static final String[] a3fields = {"surchargeName1", "surchargeFactor1", "surchargeFactorProposal1",
				      "surchargeName2", "surchargeFactor2", "surchargeFactorProposal2",
				      "surchargeName3", "surchargeFactor3", "surchargeFactorProposal3",
				      "surchargeName4", "surchargeFactor4", "surchargeFactorProposal4",
				      "surchargeName5", "surchargeFactor5", "surchargeFactorProposal5"};

    static final String a4 = "#A4 ";
    static final String[] a4fields = {"shippingAbsolute", "shippingFactor",
				      "handlingAbsolute", "handlingFactor",
				      "unknown_a4_1", "unknown_a4_2", 
				      "unknown_a4_3", "unknown_a4_4",
				      "unknown_a4_5", "unknown_a4_6"};

    /* Materialgruppenkalk */
    static final String[] mgfields = {"mgName",
				      "unknown_1", 
				      "mgCalcListpriceBased", 
				      "mgOverheadCostFactor", 
				      "mgFactor1", "mgFactor2", "mgFactor3"};

    static final String a01 = "#A01";
    static final String[] a01fields = mgfields;

    static final String a02 = "#A02";
    static final String[] a02fields = mgfields;

    static final String a03 = "#A03";
    static final String[] a03fields = mgfields;

    static final String a04 = "#A04";
    static final String[] a04fields = mgfields;

    static final String a05 = "#A05";
    static final String[] a05fields = mgfields;

    static final String a06 = "#A06";
    static final String[] a06fields = mgfields;

    static final String a07 = "#A07";
    static final String[] a07fields = mgfields;

    static final String a08 = "#A08";
    static final String[] a08fields = mgfields;

    static final String a09 = "#A09";
    static final String[] a09fields = mgfields;

    static final String a10 = "#A10";
    static final String[] a10fields = mgfields;

    static final String a11 = "#A11";
    static final String[] a11fields = mgfields;

    static final String a12 = "#A12";
    static final String[] a12fields = mgfields;

    static final String a13 = "#A13";
    static final String[] a13fields = mgfields;

    static final String a14 = "#A14";
    static final String[] a14fields = mgfields;

    static final String a15 = "#A15";
    static final String[] a15fields = mgfields;

    static final String a16 = "#A16";
    static final String[] a16fields = mgfields;

    static final String a17 = "#A17";
    static final String[] a17fields = mgfields;

    static final String a18 = "#A18";
    static final String[] a18fields = mgfields;

    static final String a19 = "#A19";
    static final String[] a19fields = mgfields;

    /* Lohngruppenkalk */
    static final String[] lgfields = {"wgName",
				      "unknown_1",
				      "wgTimeFactor",
				      "wgCostPerHour",
				      "wgOverheadCostFactor",
				      "wgFactor1", "wgFactor2",
				      "wgPricePerHourHardCoded"};
    static final String a21 = "#A21";
    static final String[] a21fields = lgfields;

    static final String a22 = "#A22";
    static final String[] a22fields = lgfields;

    static final String a23 = "#A23";
    static final String[] a23fields = lgfields;

    static final String a24 = "#A24";
    static final String[] a24fields = lgfields;

    static final String a25 = "#A25";
    static final String[] a25fields = lgfields;

    static final String a26 = "#A26";
    static final String[] a26fields = lgfields;

    static final String a27 = "#A27";
    static final String[] a27fields = lgfields;

    static final String a28 = "#A28";
    static final String[] a28fields = lgfields;

    static final String a29 = "#A29";
    static final String[] a29fields = lgfields;

    static final String a30 = "#A30";
    static final String[] a30fields = lgfields;

    static final String a31 = "#A31";
    static final String[] a31fields = lgfields;

    static final String a32 = "#A32";
    static final String[] a32fields = lgfields;

    static final String a33 = "#A33";
    static final String[] a33fields = lgfields;

    static final String a34 = "#A34";
    static final String[] a34fields = lgfields;

    static final String a35 = "#A35";
    static final String[] a35fields = lgfields;

    static final String a36 = "#A36";
    static final String[] a36fields = lgfields;

    static final String a37 = "#A37";
    static final String[] a37fields = lgfields;

    static final String a38 = "#A38";
    static final String[] a38fields = lgfields;

    static final String a39 = "#A39";
    static final String[] a39fields = lgfields;


    /* Text Projekt */
    static final String tpx = "#TPX";
    static final String[] tpxfields = {"projecttext"};

    /* Text T1 */
    static final String tt1 = "#TT1";
    static final String[] tt1fields = {"t1text"};

    /* Text T2 */
    static final String tt2 = "#TT2";
    static final String[] tt2fields = {"t2text"};

    /* Text T3 */
    static final String tt3 = "#TT3";
    static final String[] tt3fields = {"t3text"};

    /* Text TE */
    static final String tte = "#TTE";
    static final String[] ttefields = {"tetext"};

    /* Text Briefkopf */
    static final String tbk = "#TBK";
    static final String[] tbkfields = {"tbktext"};

    /* Text Firmenstempel */
    static final String tfs = "#TFS";
    static final String[] tfsfields = {"tfstext"};

    /* Positionsarten */
    static final String[] posfields = {"unknown_1", 
				       "oz", "clientoz", 
				       "name", "goodsgroup", 
				       "ordernumber", "supplier", 
				       "suppliernumber",
				       "manufacturer", "unit", 
				       "unknown_2", "unknown_3", "unknown_4", "unknown_5", 
				       "circa", 
				       "formfeed", 
				       "shortTextLinesCount", 
				       "allTextLinesCount"};

    static final String[] posfields3_52 = {"unknown_1", 
					   "oz",
					   "name", "goodsgroup", 
					   "ordernumber", "supplier", 
					   "suppliernumber",
					   "manufacturer", "unit", 
					   "unknown_2", "unknown_3", "unknown_4", "unknown_5", 
					   "circa", 
					   "formfeed", 
					   "shortTextLinesCount", 
					   "allTextLinesCount"};

    static final String pp = "#PP"; // P-Position
    static String[] ppfields = posfields;

    static final String pi = "#PI"; // I-Position
    static String[] pifields = posfields;

    static final String pa = "#PA"; // A-Position
    static String[] pafields = posfields;

    static final String pe = "#PE"; // E-Position
    static String[] pefields = posfields;

    static final String pm = "#PM"; // M-Position
    static String[] pmfields = posfields;

    static final String pu = "#PU"; // U-Position
    static String[] pufields = posfields;

    static final String pq = "#PQ"; // Q-Position
    static String[] pqfields = posfields;

    static final String pr = "#PR"; // R-Position
    static String[] prfields = posfields;

    static final String ps = "#PS"; // S-Position
    static String[] psfields = posfields;

    static final String[] pzfieldsDefault = {"unknown_1", 
					     "oz", "clientoz", 
					     "name", 
					     "unknown_2", "unknown_3",
					     "formfeed", 
					     "shortTextLinesCount", 
					     "allTextLinesCount"};

    static final String[] pzfields3_52 = {"unknown_1", 
					  "oz",
					  "name", 
					  "unknown_2", "unknown_3",
					  "formfeed", 
					  "shortTextLinesCount", 
					  "allTextLinesCount"};

    static final String pz = "#PZ"; // Z-Position
    static String[] pzfields = pzfieldsDefault;

    static final String[] titlefields = {"unknown_1", 
					 "oz", "clientoz", 
					 "name", 
					 "unknown_2", "unknown_3", "unknown_4",
					 "formfeed", 
					 "shortTextLinesCount", 
					 "allTextLinesCount"};

    static final String[] titlefields3_52 = {"unknown_1", 
					     "oz",
					     "name", 
					     "unknown_2", "unknown_3", "unknown_4",
					     "formfeed", 
					     "shortTextLinesCount", 
					     "allTextLinesCount"};

    static final String p1 = "#P1"; // 1-Position
    static String[] p1fields = titlefields;

    static final String p2 = "#P2"; // 2-Position
    static String[] p2fields = titlefields;

    static final String p3 = "#P3"; // 3-Position
    static String[] p3fields = titlefields;

    static final String p4 = "#P4"; // 4-Position
    static String[] p4fields = titlefields;

    static final String px = "#PX"; // X-Position

    static final String[] pxfieldsDefault = {"unknown_1", 
				      "oz", "clientoz", 
				      "formfeed", 
				      "shortTextLinesCount", 
				      "allTextLinesCount"};

    static final String[] pxfields3_52 = {"unknown_1", 
					  "oz",
					  "formfeed", 
					  "shortTextLinesCount", 
					  "allTextLinesCount"};



    static final String py = "#PX"; // Memofelds
    static String[] pxfields = pxfieldsDefault;

    static final String f1 = "#F1"; // erweiterte Artikeldaten (nicht bei 1234X)
    static String[] f1fields = {"marker", 
				      "directSellingPrice", 
				      "netpos", 
				      "rounding", 
				      "markZ", 
				      "surchargeFactor", 
				      "materialGroup", "materialPurchasePrice", 
				      "materialSupplierFactor", "materialPurchaseDiscount", 
				      "materialSellingPrice", "materialFactor", 
				      "wagePurchasePrice", 
				      "wageSellingPrice", "wageGroup", 
				      "wageGroup1", "wageGroup2",
				      "wageGroup3", "wageGroup4",
				      "wageGroup5", "wageMinutes1", 
				      "wageMinutes2", "wageMinutes3", 
				      "wageMinutes4", "wageMinutes5",
				      "wageFactor", "amount"};

    static final String f2 = "#F2"; // erweiterte Artikeldaten (nicht bei 1234X)
    static final String[] f2fields = {"materialUnitprice_wo_addons",
				      "unknown_1", "unknown_2", "unknown_3", "unknown_4", "unknown_5",
				      "materialPurchasePrice_ignored", "materialUnitPrice_ignored", 
				      "wageUnitprice_wo_addons",
				      "wageUnitprice1_wo_addons",
				      "wageUnitprice2_wo_addons",
				      "wageUnitprice3_wo_addons",
				      "wageUnitprice4_wo_addons",
				      "wageUnitprice5_wo_addons",
				      "wagePurchaseCost_reverseCalced",
				      "wageUnitprice",
				      "addonNumber1", "addonValue1",
				      "addonNumber2", "addonValue2",
				      "addonNumber3", "addonValue3",
				      "addonNumber4", "addonValue4",
				      "addonNumber5", "addonValue5",
				      "addonNumber6", "addonValue6",
				      "addonNumber7", "addonValue7",
				      "addonNumber8", "addonValue8",
				      "addonNumber9", "addonValue9",
				      "addonNumber10", "addonValue10",
				      "addonNumber11", "addonValue11",
				      "addonNumber12", "addonValue12",
				      "addonNumber13", "addonValue13",
				      "addonNumber14", "addonValue14",
				      "addonNumber15", "addonValue15",
				      "addonNumber16", "addonValue16",
				      "addonNumber17", "addonValue17",
				      "addonNumber18", "addonValue18",
				      "addonNumber19", "addonValue19",
				      "addonNumber20", "addonValue20"};

    static final String X = "#X"; // Ende Marker
    static final String DEFAULT_VERSION = "BauManWin V3.89";

	ArrayList<BaumanFormatVersion> bm_formats;
    
    protected AbstractBaumanAdapter(String bm_versions[]) throws IOException{
		bm_formats = new ArrayList<BaumanFormatVersion>();
		for(String bm_version:bm_versions){
			ClassLoader ctxldr = Thread.currentThread().getContextClassLoader();
			InputStream defs = ctxldr.getResourceAsStream(bm_version);
			BufferedReader br=new BufferedReader(new InputStreamReader(defs));
			BaumanFormatVersion bmfv = new BaumanFormatVersion(br);
			bm_formats.add(bmfv);
		}
    }
    
	public void write(DocumentPart doc, ParseContext ctx) throws FormatAdapterException
	{
			try {
				for(BaumanFormatVersion v:bm_formats){
					if (v.valid_for.equals(DEFAULT_VERSION)){
						ctx.bmfv = v;
						break;
					}
				}

				emitStart(ctx);
				ctx.lineCounter = 0;
				for (String marker: new String[]{
						k01,k02,k03,k04,k05,k06,k07,k08,k09,k10,k11,k12,
						a1,a2,a3,a4,
						a01,a02,a03,a04,a05,a06,a07,a08,a09,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,
						a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33,a34,a35,a36,a37,a38,a39}){
					
					writeLine (marker, ctx);
				}
				writeMemo (tpx, ctx);
				writeMemo (tt1, ctx);
				writeMemo (tt2, ctx);
				writeMemo (tt3, ctx);
				writeMemo (tte, ctx);
				writeMemo (tbk, ctx);
				writeMemo (tfs, ctx);
				writePositions (JBMDocumentNames.POSITIONS, ctx);
				emitDone(ctx);
			} catch (DocumentPartException e) {
				System.err.println (ctx.requestDocPart + " was not found!");
				throw new BMFormatException (e.toString());
			} catch (PropertyMissingException e) {
				System.err.println (ctx.requestDocPart + ": Property " + ctx.requestField + " was not found!");
			} catch (Exception e) {
				throw new BMFormatException (e.toString());
			}
	}

	private DocumentPart getDocPart (String partName, ParseContext ctx)
	throws DocumentException
	{
		ctx.requestDocPart = partName;
		return ctx.doc.getPart (partName);
	}
	
	private void assembleLine (String marker, String[] fields, DocumentPart docpart, String fieldSep, String lineSep, int shorter, ParseContext ctx)
	throws Exception
	{
		String field;
		for (int i=0; i < fields.length-shorter; i++) {
			ctx.requestField = fields[i];
			//System.out.println(requestField);
			field = docpart.getProperty (ctx.requestField).getValue().toString();
			emitField(marker, ctx.requestField, field, ctx);
		}
	}

	private void writeLine (String marker, ParseContext ctx) 
	throws Exception
	{
		String partName = ctx.bmfv.getPartname(marker);
		String[] fields = ctx.bmfv.getLinedef(marker);

		emitLineStart(marker, partName, fields, ctx);
		DocumentPart docpart = getDocPart (partName, ctx);
		assembleLine (marker, fields, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
		emitLineEnd(marker, partName, fields, ctx);
	}

	private void writeMemo (String marker, ParseContext ctx)
	throws Exception
	{
		String partName = ctx.bmfv.getPartname(marker);
		String[] fields = ctx.bmfv.getLinedef(marker);
		
		emitMemoStart(marker, partName, fields, ctx);
		DocumentPart docpart = getDocPart (partName, ctx);
		ctx.requestField = fields[0];
		String memo = docpart.getProperty (ctx.requestField).getValue().toString();
		emitMemoField(marker, ctx.requestField, memo, ctx);
		emitMemoEnd(marker, partName, fields, ctx);
	}

	private void writePositions (String partName, ParseContext ctx)
	throws Exception
	{
		emitPositionlistStart(ctx);
		DocumentPart dp = getDocPart (partName, ctx);
		ArrayList<? extends AbstractDocumentPart> poslist = dp.getPartList(JBMDocumentNames.POSITIONLIST);
		String line;

		for (DocumentPart docpart : poslist )
		{
			ctx.requestField = "poskind";
			String kennung = docpart.getProperty (ctx.requestField).getValue().toString();
			String marker = "#P"+kennung;
			String posPartName = ctx.bmfv.getPartname(marker);
			String [] fields = ctx.bmfv.getLinedef(marker);
			if (fields == null)
				throw new BMFormatException ("unbekanntes Positionsfeld '"+kennung+"' gefunden.");

			emitPositionStart(marker, posPartName, fields, ctx);
			String shortText="";
			String longText="";
			String[] shortTextLines={""};
			String[] longTextLines={""};
			int textlen;

			textlen = 0;

			// haben wir text?

			try {

				try {
					ctx.requestField = "shortText";
					shortText = docpart.getProperty (ctx.requestField).getValue().toString();
					shortTextLines = shortText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				try {
					ctx.requestField = "longText";
					longText = docpart.getProperty (ctx.requestField).getValue().toString();
					longTextLines = longText.split("\n", -1);
				} catch (PropertyMissingException ex) {}

				textlen = shortTextLines.length-1 + longTextLines.length-1;
				emitPositionMandatoryStart(marker, posPartName, fields, ctx);
				assembleLine (marker, fields, docpart, FIELDSEPARATOR, "", 2, ctx); // ohne textlänge
				emitField(marker, "shortTextLinesCount", (shortTextLines.length < 2 ? "" : ""+(shortTextLines.length-1)), ctx);
				emitField(marker, "allTextLinesCount", (textlen == 0 ? "" : ""+textlen), ctx);
				emitPositionMandatoryEnd(marker, posPartName, fields, ctx);

				// haben wir #F{1|2} Positionen?
				if ("PIAEMUQRS".indexOf (kennung) != -1)
				{
					String[] f = ctx.bmfv.getLinedef(f1);
					emitF1Start(marker, posPartName, f, ctx);
					assembleLine (f1, f, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
					emitF1End(marker, posPartName, f, ctx);

					f = ctx.bmfv.getLinedef(f2);
					emitF2Start(marker, posPartName, f, ctx);
					assembleLine (f2, f, docpart, FIELDSEPARATOR, LINESEPARATOR, 0, ctx);
					emitF2End(marker, posPartName, f, ctx);
				}

				if (textlen > 0) {
					emitPositionShortText(marker, "shortText", shortText, ctx);
					emitPositionLongText(marker, "longText", longText, ctx);
				}

				emitPositionEnd(marker, posPartName, fields, ctx);
				
			} catch (OutOfMemoryError ex) {
				System.out.println ("gotcha at line" + ctx.lineCounter);
				throw ex;
			}
		}
		emitPositionlistEnd(ctx);

	}
	
	protected void emitLineStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	protected void emitField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{}
	protected void emitLineEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitMemoStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	protected void emitMemoField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{}
	protected void emitMemoEnd(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{}
	
	protected void emitStart(ParseContext ctx) throws Exception{}
	protected void emitDone(ParseContext ctx) throws Exception{}
	

	protected void emitPositionlistStart(ParseContext ctx) throws Exception{}
	protected void emitPositionlistEnd(ParseContext ctx) throws Exception{}

	protected void emitPositionStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionMandatoryStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitPositionMandatoryEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitF1Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF1End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF2Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}
	protected void emitF2End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{}

	protected void emitPositionShortText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{}
	protected void emitPositionLongText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{}
	
}
