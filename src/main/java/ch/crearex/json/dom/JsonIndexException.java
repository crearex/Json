package ch.crearex.json.dom;

import ch.crearex.json.JsonException;

/**
 * Thrown when accessing a {@link JsonArray} fails (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonIndexException extends JsonException {
	private static final long serialVersionUID = 4950718381970311280L;
	JsonIndexException(String message) {
		super(message);
	}
}
