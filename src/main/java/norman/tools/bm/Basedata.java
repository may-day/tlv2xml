package norman.tools.bm;

public class Basedata extends JBMDocumentPart {

String[] fields = new String[]{	"createdBy","bizarrePosCounter","unknown_1","keepPosText","lockStructure","currency","unknown_2","unknown_3","unknown_4","orderType","unknown_5","clientType","netCalc","ozMask","labelTitle1","labelTitle2","labelTitle3","labelTitle4","salesPriceYearTable","unknown_6","unknown_7",
		"proposalID","unknown_8","unknown_9","projectID","projectName1","projectName2",
		"constructionSiteLine1","constructionSiteLine2","constructionSiteLine3",
		"clientid","addressName1","addressName2","addressName3","addressStreet","addressCity","addressCountry","addressZipcode",
		"deliveryName1","deliveryName2","deliveryName3","deliveryStreet","deliveryCity","deliveryCountry","deliveryZipcode","unknown_11",
		"clientRequest","clientOrderId","deliveryConditions","shippingType",
		"warranty","proposalFixationTimeLimit","deliveryDate","deliveryDateText","processor","signature1","signature2","printoutType","printoutSettings",
		"paymentMode","VATtext","VATidx","VATproz","paymentConditionText","paymentConditionDays1","paymentConditionDays2","paymentConditionPercent","paymentConditionDaysNetText","paymentConditionDaysNet",
		"kindText","object_gewerk","chances","unknown_12","projectManager","lumpOrder","desiredDate","contractNumber","unknown_13","officecode","unknown_14","unknown_15","unknown_16","officecode_sellercode","unknown_17","unknown_18","unknown_19","unknown_20","unknown_21","unknown_22","unknown_23","unknown_24","unknown_25","unknown_26","unknown_27","unknown_28","unknown_29","unknown_30","unknown_31","unknown_32","unknown_33","unknown_34","unknown_35",
		"dateBOQCreation","userBOQCreation","dateBOQChange","userBOQChange","dateProposalSubmittalLetter","userProposalSubmittalLetter","dateOrderConfirmation","userOrderConfirmation","dateBOQLock","userBOQLock","datePLReadIn","userPLReadIn","dateGAEBTransmission","userGAEBTransmission","dateCostRecalc","userCostRecalc","dateInvoice","unknown_36","unknown_37","unknown_38",
		"clientCommissionId","signatureSalutation","key1","key2"};

public Basedata(){
	super();
	for(String fieldname : fields){
		putProperty(fieldname, new StringProperty(new StringBuffer(), true, false));
	}

}
}
