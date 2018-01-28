package ch.crearex.json.schema;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonContextAdapter;
import ch.crearex.json.JsonSchemaCallback;

public class JsonSchemaContextImpl extends JsonContextAdapter implements JsonSchemaContext {

	private final JsonSchemaCallback schemaCallback;
	private final URL schemaOriginUrl;
	
	// Lazy creation
	private ArrayList<JsonSchemaValidationException> validationExceptions = null;
	
	public JsonSchemaContextImpl(JsonSchemaCallback schemaCallback, URL jsonSchemaOriginUrl) {
		this(null, schemaCallback, jsonSchemaOriginUrl);
	}
	
	public JsonSchemaContextImpl(JsonContext adaptedContext, JsonSchemaCallback schemaCallback, URL jsonSchemaOriginUrl) {
		super(adaptedContext);
		this.schemaCallback = schemaCallback;
		this.schemaOriginUrl = jsonSchemaOriginUrl;
	}

	@Override
	public boolean hasSchemaOriginUrl() {
		return schemaOriginUrl != null;
	}

	@Override
	public URL getSchemaOriginUrl() {
		return schemaOriginUrl;
	}

	@Override
	public void notifySchemaViolation(JsonSchemaValidationException violation) {
		if(validationExceptions == null) {
			validationExceptions = new ArrayList<JsonSchemaValidationException>();
		}
		validationExceptions.add(violation);
		schemaCallback.schemaViolation(violation);
	}
	
	@Override
	public boolean hasValidationErrors() {
		return validationExceptions != null;
	}
	
	@Override
	public List<JsonSchemaValidationException> getValidationExceptions() {
		return Collections.unmodifiableList(validationExceptions);
	}

}
