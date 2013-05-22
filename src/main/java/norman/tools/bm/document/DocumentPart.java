package norman.tools.bm.document;

import java.util.ArrayList;

import norman.tools.bm.DocumentException;

/*
 * Document{
 * 
 *    DocumentPart{
 *        DocumentPartList[ DocumentPart, DocumentPart, DocumentPart, ... ]
 *        DocumentPartList[ DocumentPart, DocumentPart, DocumentPart, ... ]
 *    }
 *
 *    DocumentPart{
 *    }
 */
public interface DocumentPart
{
	DocumentPart getPart (String name) throws DocumentException;
	DocumentPart addPart (String name) throws DocumentException;
	
	ArrayList<? extends AbstractDocumentPart> getPartList (String name) throws DocumentException;
	ArrayList<? extends AbstractDocumentPart> addPartList (String name);
	DocumentPart addPartToList(String listName, String partKind) throws DocumentException;
	
    Property getProperty (String name) throws DocumentException;
    // wasNotInSourceDoc .. a format adapter expected a certain value from its source, but there was no data
    // if so this value is true and hence a hint for the adapter when saving that there was no data in the beginning
    // when this could happen?
    // Imagine you feed an adapter a version of a document which is too old for the adapter to recognize
    // Since a newer doc specification could define an element which the old one did not have.
    // This way when saving the adapter could save in the original version (by skipping these values)
    void putProperty (String name, Object value, boolean keepdirty, boolean wasNotInSourceDoc);
}
