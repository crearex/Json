package ch.crearex.json.dom;

import ch.crearex.json.JsonException;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;

/**
 * Thrown when accessing a {@link JsonSimpleValue} or a {@link JsonPath} fails (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonAccessException extends JsonException {
	private static final long serialVersionUID = -2194209294197959606L;
	JsonAccessException(String message) {
		super(message);
	}
}
