package norman.tools.bm.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import norman.tools.bm.DocumentException;
import norman.tools.bm.JBMDocumentPart;

abstract public class AbstractDocumentPart<DOCPART extends DocumentPart<DOCPART>> implements DocumentPart<DOCPART> {
	
	protected HashMap<String, DOCPART> docparts;
	protected HashMap<String, ArrayList<DOCPART>> partlists;

	protected DOCPART createDocumentPart (String partName) { return null ; }
	protected ArrayList<DOCPART> createDocumentPartList (String partName) { return null ; }
	
	public DOCPART getPart (String name) throws DocumentPartMissingException{
		String[] partPath = name.split("\\.", 2);

		if (partPath.length > 0){
			DOCPART p;
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
		throw new IllegalArgumentException("Partname '"+name + "' is malformed" );
	}
	
	public DOCPART addPart (String name) throws DocumentPartMissingException{
		String[] partPath = name.split("\\.", 2);

		if (partPath.length > 0){
			DOCPART p;
			String partName = partPath[0];
			try {
				p = getPart (partName);
			} catch (DocumentPartMissingException ex) {
				p = createDocumentPart (partName);
				docparts.put(name, p);
			}
			if (partPath.length > 1){
				p = p.addPart(partPath[1]);
			}
			return p;
		}
		throw new IllegalArgumentException("Partname '"+name + "' is malformed" );
	}

	public ArrayList<DOCPART> getPartList (String name) throws DocumentPartMissingException{
		ArrayList<DOCPART> pl;
		pl = partlists.get (name);
		if (pl == null) {
			throw new DocumentPartMissingException (name);
		}
		return pl;
	}
	
	public ArrayList<DOCPART> addPartList (String name){
		ArrayList<DOCPART> pl;
		try {
			pl = getPartList (name);
		} catch (DocumentPartMissingException ex) {
			pl = createDocumentPartList(name);
			partlists.put (name, pl);
		}
		return pl;
	}

	public DOCPART addPartToList(String listName, String partKind) throws DocumentPartMissingException{
		ArrayList<DOCPART> list = getPartList(listName);
		DOCPART part = createDocumentPart(partKind);
		list.add(part);
		return part;
	}
	
	public Property getProperty (String name) throws DocumentException{
		return null;
	}
	public void putProperty (String name, Object value, boolean keepdirty, boolean wasNotInSourceDoc){
		
	}
	
	public Iterator<DOCPART> getContainedParts() {
		return null;
	}
	
	public  Iterator<ArrayList<DOCPART>> getContainedPartLists(){
		return null;
	}

}


