package ch.crearex.json;

/**
 * Thrown when building a JSON Document fails (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonBuilderException extends JsonException {

	private static final long serialVersionUID = 4224242826923681777L;
	
	public JsonBuilderException(String message, Throwable th) {
		super(message, th);
	}

	public JsonBuilderException(String message) {
		super(message);
	}
}
