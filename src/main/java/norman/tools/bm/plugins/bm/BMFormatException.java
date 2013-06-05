package norman.tools.bm.plugins.bm;

import norman.tools.bm.plugins.FormatAdapterException;

public class BMFormatException extends FormatAdapterException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BMFormatException(String message) {
	super (message);
    }

    public BMFormatException(Throwable t) {
    	super (t);
    }

    public BMFormatException(String message, Throwable t) {
    	super (message, t);
    }
}