package ch.crearex.json.schema;

import ch.crearex.json.JsonPath;

public class JsonSchemaValidationException extends JsonSchemaException {

	private static final long serialVersionUID = -4708210958867467008L;

	public JsonSchemaValidationException(JsonPath path, String message) {
		super(message);
		setPath(path);
	}
	
	public JsonSchemaValidationException(JsonPath path, String message, Throwable th) {
		super(message, th);
		setPath(path);
	}

}
