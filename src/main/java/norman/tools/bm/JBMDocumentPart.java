package norman.tools.bm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.DocumentPart;
import norman.tools.bm.document.DocumentPartMissingException;
import norman.tools.bm.document.DocumentPartNameMalformedException;
import norman.tools.bm.document.Property;
import norman.tools.bm.document.PropertyMissingException;

class JBMProperty implements Property {
	boolean fromSrcDoc;
	Object value;
	boolean dirty;

	public JBMProperty (Object val, boolean keepdirty, boolean wasNotInSourceDoc) {
		value = val;
		fromSrcDoc = !wasNotInSourceDoc;
		dirty = !keepdirty;
	}

	public Object getValue () { return value; }
	public boolean isDirty () { return dirty; }
	public boolean isFromSource() { return fromSrcDoc; }
	public String toString () { return value.toString(); }
	public void setValue (Object val, boolean keepdirty, boolean wasNotInSourceDoc) {
		value = val;
		fromSrcDoc = !wasNotInSourceDoc;
		dirty = !keepdirty;
	}

}

class StringProperty extends JBMProperty {
	StringBuffer s;

	public StringProperty (Object val, boolean keepdirty, boolean wasNotInSourceDoc) {
		super (new StringBuffer(val.toString()), keepdirty, wasNotInSourceDoc);

		s = (StringBuffer)value;
	}


	public StringProperty (StringBuffer val, boolean keepdirty, boolean wasNotInSourceDoc) {
		super (val, keepdirty, wasNotInSourceDoc);

		s = val;
	}

	public void setValue (Object v) {
		s.setLength(0);
		if (v != null)
			s.append (v.toString());
		dirty = true;
	}

	public void setValue (Object val, boolean keepdirty, boolean wasNotInSourceDoc) {
		s.setLength(0);
		if (val != null)
			s.append (val.toString());
		fromSrcDoc = !wasNotInSourceDoc;
		dirty = !keepdirty;
	}

}

public class JBMDocumentPart extends AbstractDocumentPart 
{
	HashMap<String, JBMProperty> properties;
	HashMap<String, JBMDocumentPart> docparts;
	HashMap<String, ArrayList<JBMDocumentPart>> partlists;

	public JBMDocumentPart (){

		properties = new HashMap<String, JBMProperty>();
		docparts = new HashMap<String, JBMDocumentPart>();
		partlists = new HashMap<String, ArrayList<JBMDocumentPart>>();	

	}

	public JBMDocumentPart createDocumentPart (String kind) throws DocumentException {
		if (kind.equals(JBMDocumentNames.CALCPAGE)){
			return new CalcContext();
	    }else if (kind.equals(JBMDocumentNames.POSITION)){
			return new Position();
	    }
		return new JBMDocumentPart();
	}


	public Property getProperty (String name) throws PropertyMissingException
	{
		JBMProperty p=null;
		p = (JBMProperty)properties.get (name);
		if (p == null) {
			throw new PropertyMissingException (name);
		}
		return p;
	}

	public void putProperty (String name, Object value, boolean keepdirty, boolean wasNotInSourceDoc)
	{
		JBMProperty p;
		try {
			p = (JBMProperty)getProperty (name);
			p.setValue (value, keepdirty, wasNotInSourceDoc);
		} catch (PropertyMissingException e) {
			p = new JBMProperty(value, keepdirty, wasNotInSourceDoc);
			properties.put (name, p);
		}
	}

	public void putProperty (String name, JBMProperty value)
	{
		properties.put (name, value);
	}

	public JBMDocumentPart getPart (String name) throws DocumentPartMissingException
	{
		String[] partPath = name.split("\\.", 2);

		if (partPath.length > 0){
			JBMDocumentPart p;
			String partName = partPath[0];
			p = docparts.get (partName);
			if (p == null) {
				throw new DocumentPartMissingException (name);
			}
			if (partPath.length > 1){
				p = p.getPart(partPath[1]);
			}
			return p;
		}
		throw new DocumentPartMissingException (name);
	}

	public JBMDocumentPart addPart (String name) 
	throws DocumentException
	{
		String[] partPath = name.split("\\.", 2);

		if (partPath.length > 0){
			JBMDocumentPart p;
			String partName = partPath[0];
			try {
				p = getPart (partName);
			} catch (DocumentPartMissingException ex) {
				p = createDocumentPart (partName);
				docparts.put (name, p);
			}
			if (partPath.length > 1){
				p = p.addPart(partPath[1]);
			}
			return p;
		}
		throw new DocumentPartNameMalformedException(name);
	}

	public ArrayList<JBMDocumentPart> getPartList (String name) 
	throws DocumentPartMissingException
	{
		ArrayList<JBMDocumentPart> pl;
		pl = partlists.get (name);
		if (pl == null) {
			throw new DocumentPartMissingException (name);
		}
		return pl;

	}

	public DocumentPart addPartToList(String listName, String partKind) 
	throws DocumentException{
		ArrayList<JBMDocumentPart> list = getPartList(listName);
		JBMDocumentPart part = createDocumentPart(partKind);
		list.add(part);
		return part;
	}
	
	public ArrayList<JBMDocumentPart> addPartList (String name)
	{

		ArrayList<JBMDocumentPart> pl;
		try {
			pl = getPartList (name);
		} catch (DocumentPartMissingException ex) {
			pl = new ArrayList<JBMDocumentPart> ();
			partlists.put (name, pl);
		}
		return pl;
	}

	String get (String key) {
		String ret=null;
		try{
			ret=getProperty(key).getValue().toString();
		}catch(PropertyMissingException ex){

		}
		return ret;
	}

	void set (String key, Object value) throws PropertyMissingException{
		StringProperty p= (StringProperty)getProperty(key);
		p.setValue (value);
	}

	public BigDecimal str2dec(String val){
		BigDecimal d;
		try{
			val = val.replace(',', '.');
			d=new BigDecimal(val);
		}catch(NumberFormatException ex){
			System.err.println("Error converting: '"+val+"' to BigDecimal.");
			throw ex;
		}
		return d;
	}
	
	public BigDecimal dbl(String key){
		String val = get(key);
		if (val == null || val.equals("")){
			return new BigDecimal("0.00");
		}
		return str2dec(val);
	}

	public BigDecimal dblCent(String key){
		String val = get(key);
		if (val == null || val.equals("")){
			return new BigDecimal("0.00");
		}
		return str2dec(val).movePointLeft(2);
	}

	public BigDecimal dblFak(String key){
		String val = get(key);
		if (val == null || val.equals("")){
			return new BigDecimal("1.00");
		}
		return str2dec(val);
	}

}

