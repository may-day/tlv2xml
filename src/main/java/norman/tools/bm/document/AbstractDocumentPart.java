package norman.tools.bm.document;

import java.util.ArrayList;

import norman.tools.bm.DocumentException;

abstract public class AbstractDocumentPart implements DocumentPart {
	public DocumentPart getPart (String name) throws DocumentException{
		return null;
	}
	public DocumentPart addPart (String name) throws DocumentException{
		return null;
	}

	public ArrayList<? extends AbstractDocumentPart> getPartList (String name) throws DocumentException{
		return null;
	}
	public ArrayList<? extends AbstractDocumentPart> addPartList (String name){
		return null;
	}

	public Property getProperty (String name) throws DocumentException{
		return null;
	}
	public void putProperty (String name, Object value, boolean keepdirty, boolean wasNotInSourceDoc){
		
	}
}


