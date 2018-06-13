package ch.crearex.json.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SchemaConstants;

public class JsonDocument implements Cloneable {

	private JsonContainer root = null;
	private SchemaValidationStatus validationStatus = SchemaValidationStatus.NOT_VALIDATED;
	private List<JsonSchemaValidationException> validationErrors = null;
	
	public static JsonDocument createEmptyDocument() {
		return new JsonDocument();
	}
	
	public static JsonDocument createObjectDocument() {
		return new JsonDocument(new JsonObject(null));
	}
	
	public static JsonDocument createArrayDocument() {
		return new JsonDocument(new JsonArray(null));
	}
	
	public JsonDocument clone() {
		JsonDocument clone = new JsonDocument();
		clone.root = root.clone();
		clone.validationStatus = validationStatus;
		
		if(validationErrors != null) {
			clone.validationErrors = new ArrayList<JsonSchemaValidationException>();
			for(JsonSchemaValidationException ex: validationErrors) {
				clone.validationErrors.add(ex);
			}
		}
		
		return clone;
	}
	
	/**
	 * Use the static factory methods to create a JSON Document.
	 */
	public JsonDocument() {
	}
	
	/**
	 * Use the static factory methods to create a JSON Document.
	 */
	public JsonDocument(JsonContainer root) {	
		this.root = root;
	}
		
	public JsonDocument setRoot(JsonContainer root) {
		this.root = root;
		return this;
	}
	
	public JsonContainer getRoot() {
		return root;
	}
	
	public boolean isObjectRoot() {
		return root instanceof JsonObject;
	}
	
	public boolean isArrayRoot() {
		return root instanceof JsonArray;
	}
	
	public JsonObject getRootObject() {
		if(root instanceof JsonObject) {
			return (JsonObject)root;
		}
		throw new JsonDomAccessException("Illegal Type: The JSON Root Object is a " + root.getTypeName() + " (Expected: "+SchemaConstants.OBJECT_TYPE+")");
	}

	public JsonArray getRootArray() {
		if(root instanceof JsonArray) {
			return (JsonArray)root;
		}
		throw new JsonDomAccessException("Illegal Type: The JSON Root Object is a " + root.getTypeName() + " (Expected: "+SchemaConstants.ARRAY_TYPE+")");
	}
	
	public JsonDocument traverse(JsonCallback callback) {
		return traverse(callback, new JsonDomContext(callback));
	}
	
	JsonDocument traverse(JsonCallback callback, JsonDomContext context) {
		if(root == null) {
			return this;
		}
		context.notifyBeginDocument(callback);
		root.traverse(context, callback);
		context.notifyEndDocument(callback);
		return this;
	}
	
	@Override
	public String toString() {
		return print();
	}
	
	public String print() {
		if(root != null) {
			return root.toString();
		}
		return "";
	}
	
	public String prettyPrint() {
		StringBuffer buffer = new StringBuffer();
		JsonPrettyFormatter formatter = new JsonPrettyFormatter(buffer);
		traverse(formatter);
		return buffer.toString();
	}

	public JsonSimpleValue getValue(JsonPath path) {
		if(root == null) {
			return null;
		}
		return root.getValue(path);
	}
	
	public List<JsonElement> query(JsonPath path) {
		
		QueryContext context = new QueryContext(path);
		if(root == null) {
			return context.getResult();
		}
		
		root.query(context);
		return context.getResult();
	}

	/**
	 * Validate the JSON Document against the JSON Schema.
	 * @see JsonParserFactory#createJsonSchema(java.net.URL)
	 */
	public SchemaValidationStatus validate(JsonSchema schema) {
		return validate(schema, new JsonSchemaCallback() {
			@Override
			public void schemaViolation(JsonSchemaValidationException violation) {
				// do supress the default exception
			}
		});
	}
	
	/**
	 * Validate the JSON Document against the JSON Schema.
	 * @see JsonParserFactory#createJsonSchema(java.net.URL)
	 */
	public SchemaValidationStatus validate(JsonSchema schema, JsonSchemaCallback schemaCallback) {
		setValidationResult(SchemaValidationStatus.NOT_VALIDATED, null);
		JsonDomContext context = new JsonDomContext(schema).setSchemaCallback(schemaCallback);
		try {
			traverse(schema, context);
			JsonSchemaContext schemaContext = context.getSchemaStack().getSchemaContext();
			if(schemaContext.hasValidationErrors()) {
				setValidationResult(SchemaValidationStatus.FAILED, schemaContext.getValidationExceptions());
			} else {
				setValidationResult(SchemaValidationStatus.VALID, null);
			}		
			return validationStatus;
		} catch(JsonSchemaValidationException e) {
			JsonSchemaContext schemaContext = context.getSchemaStack().getSchemaContext();
			setValidationResult(SchemaValidationStatus.FAILED, schemaContext.getValidationExceptions());
			throw e;
		} catch(JsonSchemaException e) {
			setValidationResult(SchemaValidationStatus.FAILED, null);
			throw e;
		} catch(Exception e) {
			setValidationResult(SchemaValidationStatus.FAILED, null);
			throw e;
		}
	}
	
	void setValidationResult(SchemaValidationStatus validationStatus, List<JsonSchemaValidationException> validationErrorMessages) {
		this.validationStatus = validationStatus;
		this.validationErrors = validationErrorMessages;
	}
	
	public SchemaValidationStatus getValidationStatus() {
		return validationStatus;
	}
	
	public List<JsonSchemaValidationException> getValidationErrors() {
		return validationErrors;
	}
	
	public boolean hasValidationErrorMessages() {
		return validationErrors != null;
	}

	public String getValidationErrorsSummary() {
		StringBuilder summary = new StringBuilder();
		if(validationErrors != null) {
			boolean first = true;
			for(JsonSchemaValidationException ex: validationErrors) {
				if(first) {
					first = false;
				} else {
					summary.append('\n');
				}
				summary.append(ex.getMessage());
			}
		}
		return summary.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (!(obj instanceof JsonDocument)) {
            return false;
        }
        JsonDocument other = (JsonDocument) obj;
        return Objects.equals(root, other.root);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(root);
	}
}
