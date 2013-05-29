package norman.tools.bm.plugins.bm;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import norman.tools.bm.DocumentException;
import norman.tools.bm.JBMDocumentNames;
import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.document.DocumentPartMissingException;
import norman.tools.bm.document.PropertyMissingException;
import norman.tools.bm.plugins.DocumentFormatAdapter;
import norman.tools.bm.plugins.FormatAdapterException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class XMLBaumanFormatAdapter extends AbstractBaumanAdapter
	implements DocumentFormatAdapter
{

    int lineCounter;

    String requestDocPart, requestField;

    public XMLBaumanFormatAdapter(	String bm_versions[]) throws IOException
    {
		super(bm_versions);
    }

    public void read(InputStream is, DocumentPart doc) throws FormatAdapterException
    {
	    throw new BMFormatException ("reading not yet implemented!");
    }

    
    public void write(java.io.OutputStream os, DocumentPart doc) throws Exception
    {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          org.w3c.dom.Document document = builder.newDocument();
          Element root = document.createElement("lv");
          document.appendChild(root);
          ProcessingInstruction pi = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" encoding=\"UTF-8\" href=\"tt.xsl\" version=\"1.0\"");
          root.getParentNode().insertBefore(pi, root);          
          
    	
	    //setupDocumentVersion (DEFAULT_VERSION);
	    lineCounter = 0;
        Element kopf = document.createElement("Head");
        Element kalkblatt = document.createElement("Calc");
        root.appendChild(kopf);
        root.appendChild(kalkblatt);
	    writeLine (k01, JBMDocumentNames.HEADER, k01fields, kopf, doc);
	    writeLine (k02, JBMDocumentNames.HEADER, k02fields, kopf, doc);
	    writeLine (k03, JBMDocumentNames.HEADER, k03fields, kopf, doc);
	    writeLine (k04, JBMDocumentNames.HEADER, k04fields, kopf, doc);
	    writeLine (k05, JBMDocumentNames.HEADER, k05fields, kopf, doc);
	    writeLine (k06, JBMDocumentNames.HEADER, k06fields, kopf, doc);
	    writeLine (k07, JBMDocumentNames.HEADER, k07fields, kopf, doc);
	    writeLine (k08, JBMDocumentNames.HEADER, k08fields, kopf, doc);
	    writeLine (k09, JBMDocumentNames.HEADER, k09fields, kopf, doc);
	    writeLine (k10, JBMDocumentNames.HEADER, k10fields, kopf, doc);
	    writeLine (k11, JBMDocumentNames.HEADER, k11fields, kopf, doc);
	    writeLine (k12, JBMDocumentNames.HEADER, k12fields, kopf, doc);
	    writeLine (a1, JBMDocumentNames.CALCPAGE, a1fields, kalkblatt, doc);
	    writeLine (a2, JBMDocumentNames.CALCPAGE, a2fields, kalkblatt, doc);
	    writeLine (a3, JBMDocumentNames.CALCPAGE, a3fields, kalkblatt, doc);
	    writeLine (a4, JBMDocumentNames.CALCPAGE, a4fields, kalkblatt, doc);
	    String[] markers={a01, a02, a03, a04, a05, a06, a07, a08, a09, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19};
	    String[] partNames={"mg01", "mg02", "mg03", "mg04", "mg05", "mg06", "mg07", "mg08", "mg09", "mg10", "mg11", "mg12", "mg13", "mg14", "mg15", "mg16", "mg17", "mg18", "mg19"};
    	NumberFormat formatter = new DecimalFormat("00");
        Element mg = document.createElement("MaterialGroups");
        kalkblatt.appendChild(mg);
	    for (int i=0; i < markers.length; i++){
	        String s = formatter.format(i+1);
	        Element mgN = document.createElement("mg");
	        mg.appendChild(mgN);
	        mgN.setAttribute("id", s);
	    	writeLine (markers[i], JBMDocumentNames.CALCPAGE+"." + partNames[i], mgfields, mgN, doc);
	    }
	    markers= new String[]{a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34, a35, a36, a37, a38, a39};
	    partNames=new String []{"lg01", "lg02", "lg03", "lg04", "lg05", "lg06", "lg07", "lg08", "lg09", "lg10", "lg11", "lg12", "lg13", "lg14", "lg15", "lg16", "lg17", "lg18", "lg19"};
        Element wg = document.createElement("WageGroups");
        kalkblatt.appendChild(wg);
	    for (int i=0; i < markers.length; i++){
	        String s = formatter.format(i+1);
	        Element wgN = document.createElement("wg");
	        wg.appendChild(wgN);
	        wgN.setAttribute("id", s);
	    	writeLine (markers[i], JBMDocumentNames.CALCPAGE+"." + partNames[i], lgfields, wgN, doc);
	    }
        Element text = document.createElement("Text");
        root.appendChild(text);
	    
	    writeMemo (tpx, JBMDocumentNames.TEXT, tpxfields, text, doc);
	    writeMemo (tt1, JBMDocumentNames.TEXT, tt1fields, text, doc);
	    writeMemo (tt2, JBMDocumentNames.TEXT, tt2fields, text, doc);
	    writeMemo (tt3, JBMDocumentNames.TEXT, tt3fields, text, doc);
	    writeMemo (tte, JBMDocumentNames.TEXT, ttefields, text, doc);
	    writeMemo (tbk, JBMDocumentNames.TEXT, tbkfields, text, doc);
	    writeMemo (tfs, JBMDocumentNames.TEXT, tfsfields, text, doc);
	    writePositions (JBMDocumentNames.POSITIONS, root, doc);
	    
        // Prepare the DOM document for writing
        Source source = new DOMSource(document);
        BufferedOutputStream writer = new BufferedOutputStream(os);
        Result result = new StreamResult(writer);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        //xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        //xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //xformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        

        xformer.setOutputProperty("encoding", "UTF-8");          
        xformer.transform(source, result);

        writer.close();
	    
	} catch (DocumentPartMissingException e) {
	    System.err.println (requestDocPart + " was not found!");
	} catch (PropertyMissingException e) {
	    System.err.println (requestDocPart + ": Property " + requestField + " was not found!");
	} catch (UnsupportedEncodingException ex) {
	    throw new BMFormatException ("Kein ISO-8859-1 auf diesem System vorhanden - wird benötight um .tlv Dateien zu lesen!");
	}
/*	catch (IOException e) {
	    throw new BMFormatException (e.toString());
    }
*/    
    }
    
    private void writeln (String line, BufferedWriter writer) throws IOException
    {
	//System.out.println (line);
	writer.write (line);
	//writer.flush();
	lineCounter++;
    }

    private DocumentPart getDocPart (String partName, DocumentPart doc)
    throws DocumentException
    {
    	DocumentPart docpart;
    	String[] partPath = partName.split("\\.");
    	requestDocPart = partPath[0];
    	docpart = doc.getPart (partPath[0]);

    	for (int i=1; i < partPath.length; i++) {
    		requestDocPart = partPath[i];
    		docpart = docpart.getPart (requestDocPart);
    	}

    	return docpart;
    }

    private void assembleLine (String marker, String[] fields, DocumentPart docpart, 
    		Node parent, int shorter)
    throws DocumentException
    {
    	String fieldvalue;
    	for (int i=0; i < fields.length-shorter; i++) {
    		requestField = fields[i];
    		System.out.println("creating element " + requestField);
    		String xmlrequestField = requestField.replace("/", "_");
    		Element field = parent.getOwnerDocument().createElement(xmlrequestField);
    		//System.out.println(requestField);
    		fieldvalue = docpart.getProperty (requestField).getValue().toString();
    		field.setTextContent(fieldvalue);
    		parent.appendChild(field);
    	}
    }

    private void writeLine (String marker, String partName, 
    		String[] fields, Node parent, DocumentPart doc) 
    throws DocumentException, IOException
    {
    	DocumentPart docpart = getDocPart (partName, doc);
    	assembleLine (marker, fields, docpart, parent, 0);
    }

    private void writeMemo (String marker, String partName, String[] fields, 
    		Node parent, DocumentPart doc)
    throws DocumentException, IOException
    {
    	DocumentPart docpart = getDocPart (partName, doc);
    	requestField = fields[0];
    	Element field = parent.getOwnerDocument().createElement(requestField);
    	String memo = docpart.getProperty (requestField).getValue().toString();
    	field.setTextContent(memo);
    	parent.appendChild(field);
    }

    private void writePositions (String partName, Node parent, DocumentPart doc)
	throws DocumentException, BMFormatException, IOException
    {
    	Element docpartelement = parent.getOwnerDocument().createElement(partName);
    	DocumentPart dp = getDocPart (partName, doc);
    	ArrayList<? extends AbstractDocumentPart> poslist = dp.getPartList(JBMDocumentNames.POSITIONLIST);

    	for (DocumentPart docpart: poslist )
    	{
        	Element poselement = parent.getOwnerDocument().createElement("position");
    		requestField = "poskind";
    		String kennung = docpart.getProperty (requestField).getValue().toString();
    		String [] fields;
    		if (kennung.equals("P")) fields = ppfields;
    		else if (kennung.equals("I")) fields = pifields;
    		else if (kennung.equals("A")) fields = pafields;
    		else if (kennung.equals("E")) fields = pefields;
    		else if (kennung.equals("M")) fields = pmfields;
    		else if (kennung.equals("U")) fields = pufields;
    		else if (kennung.equals("Q")) fields = pqfields;
    		else if (kennung.equals("R")) fields = prfields;
    		else if (kennung.equals("S")) fields = psfields;
    		else if (kennung.equals("Z")) fields = pzfields;
    		else if (kennung.equals("1")) fields = p1fields;
    		else if (kennung.equals("2")) fields = p2fields;
    		else if (kennung.equals("3")) fields = p3fields;
    		else if (kennung.equals("4")) fields = p4fields;
    		else if (kennung.equals("X")) fields = pxfields;
    		else throw new BMFormatException ("unbekanntes Positionsfeld '"+kennung+"' gefunden.");

    		poselement.setAttribute("kind",kennung);
    		String shortText;
    		String longText;

    		try {

	        	Element shortTextElement = parent.getOwnerDocument().createElement("shortText");
	        	Element longTextElement = parent.getOwnerDocument().createElement("longText");
    			try {
    				requestField = "shortText";
    				shortText = docpart.getProperty (requestField).getValue().toString();
    				shortTextElement.setTextContent(shortText);
    			} catch (PropertyMissingException ex) {}

    			try {
    				requestField = "longText";
    				longText = docpart.getProperty (requestField).getValue().toString();
    				longTextElement.setTextContent(longText);
    			} catch (PropertyMissingException ex) {}

    			assembleLine ("#P"+kennung, fields, docpart, poselement, 2); // ohne textl�nge

    			// haben wir #F{1|2} Positionen?
    			if ("PIAEMUQRS".indexOf (kennung) != -1)
    			{
    	        	Element subposelement = parent.getOwnerDocument().createElement("calc");
    				assembleLine ("#F1", f1fields, docpart, subposelement, 0);

    				assembleLine ("#F2", f2fields, docpart, subposelement, 0);
    				poselement.appendChild(subposelement);
    			}
				poselement.appendChild(shortTextElement);
				poselement.appendChild(longTextElement);
    		} catch (OutOfMemoryError ex) {
    			System.out.println ("gotcha at line" + lineCounter);
    			throw ex;
    		}
    		
        	docpartelement.appendChild(poselement);
    	}
	
    	parent.appendChild(docpartelement);
    }


    private void setupDocumentVersion (String createdBy) {
    	double version = Double.parseDouble(createdBy.split(" ")[1].substring(1));

    	if (version <= 3.52) {
    		ppfields = pifields = pafields = pefields = pmfields = 
    			pufields = pqfields = prfields = psfields = posfields3_52;
    		p1fields = p2fields = p3fields = p4fields = titlefields3_52;
    		pzfields = pzfields3_52;
    		pxfields = pxfields3_52;
    	} else {
    		ppfields = pifields = pafields = pefields = pmfields = 
    			pufields = pqfields = prfields = psfields = pzfields = posfields;
    		p1fields = p2fields = p3fields = p4fields = titlefields;
    		pxfields = pxfieldsDefault;
    		pzfields = pzfieldsDefault;
    	}
    }
	

}
