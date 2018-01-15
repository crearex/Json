package ch.crearex.json;

/**
 * Root Exception for all JsonExceptions (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonException extends RuntimeException {

	private static final long serialVersionUID = -6553601293027290492L;
	
	public JsonException() {	
	}
	
	public JsonException(String message) {
		super(message);
	}
	
	public JsonException(String message, Throwable th) {
		super(message, th);
	}

}
