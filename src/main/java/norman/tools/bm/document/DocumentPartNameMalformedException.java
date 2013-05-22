package norman.tools.bm.document;

import norman.tools.bm.DocumentPartException;

public class DocumentPartNameMalformedException extends DocumentPartException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DocumentPartNameMalformedException (String name) {
    	super ("The partname '" + name + "' is malformed."); 
        }
}
