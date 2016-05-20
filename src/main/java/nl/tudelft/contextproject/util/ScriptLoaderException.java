package nl.tudelft.contextproject.util;

/**
 * Exception used by the {@link ScriptLoader} class.
 */
public class ScriptLoaderException extends Exception {

	private static final long serialVersionUID = 6789000661726857474L;
	
	/**
	 * Constructor for this exception with a custom message.
	 * @param message The message about this exception.
	 */
	public ScriptLoaderException(String message) {
		super(message);
	}

}
