package ch.crearex.json.dom;

import ch.crearex.json.JsonBuilderException;
import ch.crearex.json.JsonException;

/**
 * Thrown when accessing the root object of a {@link JsonDocument} fails (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonDomAccessException extends JsonException {

	private static final long serialVersionUID = 5132254808512621692L;

	protected JsonDomAccessException(String message, Throwable th) {
		super(message, th);
	}
	
	protected JsonDomAccessException(String message) {
		super(message);
	}
}
