package norman.tools.bm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import norman.tools.bm.document.AbstractDocumentPart;
import norman.tools.bm.document.Property;
import norman.tools.bm.document.PropertyMissingException;

abstract class JBMProperty<T> implements Property {
	T value;

	public void assign(Object obj){};
	public void init(){};

	public T get () { return value; }
	public void set (T value) { this.value = value; }
	
	public String toString () { return value.toString(); }

	public void setValue (String v) {
		assign(v);
	}

	public String getValue () {
		return toString();
	}
	
}

class StringProperty extends JBMProperty<String> {

	public void init(){ value = new String(); }
	public void assign(Object obj){
		if (obj != null)
			value = obj.toString();
		else
			value = new String();
	};

	public StringProperty(){
		init();
	}
	public StringProperty(Object value){
		super();
		assign(value);
	}
}

class DecimalProperty extends JBMProperty<BigDecimal> {

	public void init(){ value = new BigDecimal("0.00"); }
	public void assign(Object obj){
		if (obj != null)
			value = new BigDecimal(obj.toString());
		else
			value = new BigDecimal("0.00");
	};

	public DecimalProperty(){
		init();
	}
	public DecimalProperty(Object value){
		assign(value);
	}
}

public class JBMDocumentPart extends AbstractDocumentPart<JBMDocumentPart> 
{
	HashMap<String, Property> properties;
	HashMap<String, JBMDocumentPart> docparts;
	HashMap<String, ArrayList<JBMDocumentPart>> partlists;

	public JBMDocumentPart (){

		properties = new HashMap<String, Property>();

	}

	protected JBMDocumentPart createDocumentPart (String name, String kind) {
		JBMDocumentPart p;
		if (kind.equals(JBMDocumentNames.CALCPAGE)){
			p = new CalcContext();
	    }else if (kind.equals(JBMDocumentNames.POSITION)){
			p = new Position();
	    }else
		  p = new JBMDocumentPart();
		return p;
	}

	protected ArrayList<JBMDocumentPart> createDocumentPartList (String partName) { 
		return new ArrayList<JBMDocumentPart> ();
	}
	

	public Property getProperty (String name) throws PropertyMissingException
	{
		Property p=null;
		p = properties.get (name);
		if (p == null) {
			throw new PropertyMissingException (name);
		}
		return p;
	}

	public void putProperty (String name, String value)
	{
		Property p;
		try {
			p = getProperty (name);
			p.setValue (value);
		} catch (PropertyMissingException e) {
			p = new StringProperty(value);
			properties.put (name, p);
		}
	}

	public void putProperty (String name, Property value)
	{
		properties.put (name, value);
	}

	/*
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

	*/
}

