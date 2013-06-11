package norman.tools.bm.plugins.bm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import norman.tools.bm.JBMDocumentNames;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.plugins.DocumentFormatAdapter;
import norman.tools.bm.plugins.FormatAdapterException;

import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

class XMLParseContext extends ParseContext{
	public Element root, header, calcpage, wg, mg, text, positions, current, currentpos;
	public XMLParseContext(ArrayList<BaumanFormatVersion> bmversions, DocumentPart doc){
		super(bmversions,doc, null, null);
		root = header = calcpage = wg = mg = text = positions = current = currentpos = null;
	}

};

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


    protected void emitStart(ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.newDocument();
        Element root = document.createElement("lv");
        document.appendChild(root);
        ProcessingInstruction pi = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" encoding=\"UTF-8\" href=\"tt.xsl\" version=\"1.0\"");
        root.getParentNode().insertBefore(pi, root);
        xmlctx.root = root;

        xmlctx.header = document.createElement(JBMDocumentNames.HEADER);
        xmlctx.calcpage = document.createElement(JBMDocumentNames.CALCPAGE);
        xmlctx.text = document.createElement(JBMDocumentNames.TEXT);
        xmlctx.positions = document.createElement(JBMDocumentNames.POSITIONS);
        root.appendChild(xmlctx.header);
        root.appendChild(xmlctx.calcpage);
        root.appendChild(xmlctx.text);
        root.appendChild(xmlctx.positions);

        xmlctx.mg = document.createElement("MaterialGroups");
        xmlctx.wg = document.createElement("WageGroups");
        xmlctx.calcpage.appendChild(xmlctx.mg);
        xmlctx.calcpage.appendChild(xmlctx.wg);
    }
    
	protected void emitLineStart(String marker, String partName, String[] fields, ParseContext ctx)throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
		if (JBMDocumentNames.HEADER.equals(partName)) xmlctx.current = xmlctx.header;
		else if (JBMDocumentNames.CALCPAGE.equals(partName)) xmlctx.current = xmlctx.calcpage;
		else if (partName.startsWith(JBMDocumentNames.CALCPAGE + ".mg")){
	        Element mgN = xmlctx.root.getOwnerDocument().createElement("mg");
	        xmlctx.mg.appendChild(mgN);
	        mgN.setAttribute("id", partName.substring(partName.indexOf('.')+1));
	        xmlctx.current = mgN;
		}
		else if (partName.startsWith(JBMDocumentNames.CALCPAGE + ".wg")){
	        Element wgN = xmlctx.root.getOwnerDocument().createElement("wg");
	        xmlctx.wg.appendChild(wgN);
	        wgN.setAttribute("id", partName.substring(partName.indexOf('.')+1));
	        xmlctx.current = wgN;
		}
		else xmlctx.current = null;
	}
	
	protected void emitField(String marker, String fieldname, String fieldvalue, ParseContext ctx)throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
        
		Element field = xmlctx.current.getOwnerDocument().createElement(fieldname);

		if (needsXmlSanitization(fieldvalue)){
			fieldvalue = DatatypeConverter.printBase64Binary(fieldvalue.getBytes());
			field.setAttribute("enc", "base64");
			field.appendChild(xmlctx.current.getOwnerDocument().createCDATASection(fieldvalue));
		}else
			field.setTextContent(fieldvalue);
		xmlctx.current.appendChild(field);
		
	}
	

	protected void emitLineEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
		xmlctx.current = null;
	}

	protected void emitMemoStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
        xmlctx.current = xmlctx.text;
	}
	protected void emitMemoField(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{
        //emitField(marker, fieldname, fieldvalue, ctx);
        XMLParseContext xmlctx = (XMLParseContext)ctx;
        
		Element field = xmlctx.current.getOwnerDocument().createElement(fieldname);
		if (needsXmlSanitization(fieldvalue)){
			fieldvalue = DatatypeConverter.printBase64Binary(fieldvalue.getBytes());
			field.setAttribute("enc", "base64");
		}
		field.appendChild(xmlctx.current.getOwnerDocument().createCDATASection(fieldvalue));
		xmlctx.current.appendChild(field);
	}
	
	protected void emitMemoEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineEnd(marker, partName, fields, ctx);
	}
	
    public void write(java.io.OutputStream os, DocumentPart doc) throws Exception
    {
        XMLParseContext xmlctx = new XMLParseContext(bm_formats, doc);
        write(doc, xmlctx);
    	
        // Prepare the DOM document for writing
        Source source = new DOMSource(xmlctx.root.getOwnerDocument());
        BufferedOutputStream writer = new BufferedOutputStream(os);
        Result result = new StreamResult(writer);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        //xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        //xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //xformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        

        xformer.setOutputProperty("encoding", "UTF-8");          
        xformer.transform(source, result);

        writer.close();
	    
    }

	protected void emitPositionStart(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
        Element pos = xmlctx.root.getOwnerDocument().createElement(JBMDocumentNames.POSITION);
	    xmlctx.positions.appendChild(pos);
		pos.setAttribute("kind", marker.substring(2));
	    xmlctx.current = xmlctx.currentpos = pos;
		
	}
	
	protected void emitPositionEnd(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
		emitLineEnd(marker, partName, fields, ctx);
		
        XMLParseContext xmlctx = (XMLParseContext)ctx;
	    xmlctx.currentpos = null;
	}

	protected void emitF1Start(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
        Element f1 = xmlctx.root.getOwnerDocument().createElement("calc");
	    xmlctx.current.appendChild(f1);
	    xmlctx.current = f1;
	}

	protected void emitF2End(String marker, String partName, String[] fields, ParseContext ctx) throws Exception{
        XMLParseContext xmlctx = (XMLParseContext)ctx;
	    xmlctx.current = xmlctx.currentpos;
	}

	protected void emitPositionShortText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{
		emitField(marker, fieldname, fieldvalue, ctx);
	}
	protected void emitPositionLongText(String marker, String fieldname, String fieldvalue, ParseContext ctx) throws Exception{
		emitField(marker, fieldname, fieldvalue, ctx);
	}
	
	public String xmlSanitize( String str, boolean replace_with_space ) {
		if( str == null ) return null;

		StringBuilder erg = new StringBuilder();
		char ch;

		for( int i = 0; i < str.length(); i++ ) {
			ch = str.charAt(i);
			// xml char http://www.w3.org/TR/xml/#charsets
			if(ch == 0x0009 || ch == 0x000A || ch == 0x000D || (ch >= 0x0020 && ch <= 0xD7FF ) || (ch >= 0xE000 && ch <= 0xFFFD )) 
				erg.append(ch);
			else if (replace_with_space)
				erg.append(' ');
			
		}
		return erg.toString();
	}

	public boolean needsXmlSanitization( String str ) {
		if( str != null ){
			char ch;
	
			for( int i = 0; i < str.length(); i++ ) {
				ch = str.charAt(i);
				// xml char http://www.w3.org/TR/xml/#charsets
				if(!(ch == 0x0009 || ch == 0x000A || ch == 0x000D || (ch >= 0x0020 && ch <= 0xD7FF ) || (ch >= 0xE000 && ch <= 0xFFFD ))) 
					return true;
				
			}
		}
		return false;
	}
	
}
