package ch.crearex.json.schema;

import ch.crearex.json.JsonPath;

public class ValidationResult {

	public static final ValidationResult OK = new ValidationResult();
	
	private final JsonPath path;
	private final String message;
	
	private ValidationResult() {
		path = null;
		message = null;
	}
	
	public ValidationResult(JsonPath path, String message) {
		this.path = path;
		this.message = message;
	}
	
	public JsonPath getPath() {
		return path;
	}
	
	public String getMessage() {
		return message;
	}
}
