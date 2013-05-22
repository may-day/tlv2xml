package norman.tools.bm;

import java.util.ArrayList;

public class LV extends JBMDocumentPart {

	public JBMDocumentPart header;
	public JBMDocumentPart calcpage;
	public JBMDocumentPart text;
	public JBMDocumentPart positions;
	public ArrayList<JBMDocumentPart> poslist;

	public LV() throws DocumentException{
	    	
		header = addPart (JBMDocumentNames.HEADER);
		calcpage = addPart (JBMDocumentNames.CALCPAGE);
		text = addPart (JBMDocumentNames.TEXT);
		positions = addPart(JBMDocumentNames.POSITIONS);
		poslist = positions.addPartList(JBMDocumentNames.POSITIONLIST);

    }

}
