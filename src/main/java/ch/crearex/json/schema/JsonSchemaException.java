package ch.crearex.json.schema;

import ch.crearex.json.JsonException;
import ch.crearex.json.JsonPath;

public class JsonSchemaException extends JsonException {

	private static final long serialVersionUID = 7376103861675230465L;
	private JsonPath path = null;
	
	public JsonSchemaException(String message) {
		super(message);
	}

	public JsonSchemaException(String message, Throwable th) {
		super(message, th);
	}

	public boolean hasPath() {
		return path != null;
	}
	
	public JsonPath getPath() {
		return path;
	}
	
	public JsonSchemaException setPath(JsonPath path) {
		this.path =path;
		return this;
	}
}
