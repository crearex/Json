package ch.crearex.json;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.schema.JsonSchemaValidationException;

/**
 * Callback for handling JSON Schema violations.
 * @author Markus Niedermann
 *
 */
public interface JsonSchemaCallback {
	
	/**
	 * Called when a JSON Schema violation occurred.
	 * @param violation {@link JsonSchemaValidationException} exception containing the error information.
	 *                  You can throw or suppress this {@link JsonSchemaValidationException} in your concrete 
	 *                  implementation.
	 * @see JsonDocument#getValidationStatus()
	 */
	public void schemaViolation(JsonSchemaValidationException violation);
}
