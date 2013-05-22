package norman.tools.bm.plugins.bm;

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

    /* Kalkblatt diverse Faktoren, Zu-und Abschl�ge*/
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

}
