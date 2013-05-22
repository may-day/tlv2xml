package norman.tools.bm.document;

import norman.tools.bm.DocumentPartException;

public class DocumentPartMissingException extends DocumentPartException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DocumentPartMissingException (String name) {
	super ("The section '" + name + "' is not in the document"); 
    }
}