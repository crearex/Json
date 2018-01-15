package ch.crearex.json.schema;

import java.net.URL;
import java.util.List;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSchemaCallback;

public interface JsonSchemaContext extends JsonContext {
	boolean hasSchemaOriginUrl();
	URL getSchemaOriginUrl();
	void setAdaptedContext(JsonContext context);
	void notifySchemaViolation(String errorMessage);
	boolean hasValidationErrors();
	List<String> getValidationErrorMessages();
}
