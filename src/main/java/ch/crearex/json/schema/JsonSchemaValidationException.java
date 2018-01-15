package ch.crearex.json.schema;

public class JsonSchemaValidationException extends JsonSchemaException {

	private static final long serialVersionUID = -4708210958867467008L;

	public JsonSchemaValidationException(String message) {
		super(message);
	}
	
	public JsonSchemaValidationException(String message, Throwable th) {
		super(message, th);
	}

}
