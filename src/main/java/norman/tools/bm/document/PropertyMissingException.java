package norman.tools.bm.document;

import norman.tools.bm.DocumentException;

public class PropertyMissingException extends DocumentException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyMissingException (String name) {
	super ("The property '" + name + "' is not available."); 
    }
}