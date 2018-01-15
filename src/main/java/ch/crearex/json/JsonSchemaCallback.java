package ch.crearex.json;

/**
 * Callback for handling JSON Schema violations.
 * @author Markus Niedermann
 *
 */
public interface JsonSchemaCallback {
	
	/**
	 * Called when a JSON Schema violation occurred.
	 * @param path The exact {@link JsonPath} where the violation occurred.
	 * @param errorMessage A suitable error message describing the schema violation.
	 */
	public void schemaViolation(JsonPath path, String errorMessage);
}
