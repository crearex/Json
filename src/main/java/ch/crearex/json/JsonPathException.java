package ch.crearex.json;

/**
 * Thrown when usin a {@link JsonPath} fails (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonPathException extends JsonException {
	
	private static final long serialVersionUID = -2769922397742725471L;

	private final JsonPath path;
	
	public JsonPathException(String message, JsonPath path) {
		super(message);
		this.path = path;
	}
	
	public JsonPath getPath() {
		return path;
	}
}