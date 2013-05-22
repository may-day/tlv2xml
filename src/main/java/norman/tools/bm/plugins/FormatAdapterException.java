package norman.tools.bm.plugins;

public class FormatAdapterException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormatAdapterException(String message) {
	super (message);
    }

    public FormatAdapterException(Throwable t) {
    	super (t);
        }
}
