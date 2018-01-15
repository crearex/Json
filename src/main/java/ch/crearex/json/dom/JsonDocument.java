package ch.crearex.json.dom;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SchemaStack;

import java.util.List;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;

public class JsonDocument {

	private JsonContainer root = null;
	private SchemaValidationStatus validationStatus = SchemaValidationStatus.NOT_VALIDATED;
	private List<String> validationErrorMessages = null;
	
	public static JsonDocument createEmptyDocument() {
		return new JsonDocument();
	}
	
	public static JsonDocument createObjectDocument() {
		return new JsonDocument(new JsonObject(null));
	}
	
	public static JsonDocument createArrayDocument() {
		return new JsonDocument(new JsonArray(null));
	}
	
	/**
	 * Use the static factory methods to create a JSON Document.
	 */
	protected JsonDocument() {
	}
	
	/**
	 * Use the static factory methods to create a JSON Document.
	 */
	protected JsonDocument(JsonContainer root) {	
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
		throw new JsonDomAccessException("Illegal Type: The JSON Root Object is a " + root.getClass().getSimpleName() + " (Expected: "+JsonObject.class.getSimpleName()+")");
	}

	public JsonArray getRootArray() {
		if(root instanceof JsonArray) {
			return (JsonArray)root;
		}
		throw new JsonDomAccessException("Illegal Type: The JSON Root Object is a " + root.getClass().getSimpleName() + " (Expected: "+JsonArray.class.getSimpleName()+")");
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

	/**
	 * Validate the JSON Document against the JSON Schema.
	 * @see JsonParserFactory#createJsonSchema(java.net.URL)
	 */
	public SchemaValidationStatus validate(JsonSchema schema) {
		setValidationResult(SchemaValidationStatus.NOT_VALIDATED, null);
		
		JsonDomContext context = new JsonDomContext(schema).setSchemaCallback(new JsonSchemaCallback() {
			@Override
			public void schemaViolation(JsonPath path, String errorMessage) {
				// do supress the default exception
			}
		});
		try {
			traverse(schema, context);
			JsonSchemaContext schemaContext = context.getSchemaStack().getSchemaContext();
			if(schemaContext.hasValidationErrors()) {
				setValidationResult(SchemaValidationStatus.INVALID, schemaContext.getValidationErrorMessages());
			} else {
				setValidationResult(SchemaValidationStatus.VALID, null);
			}		
			return validationStatus;
		} catch(JsonSchemaValidationException e) {
			JsonSchemaContext schemaContext = context.getSchemaStack().getSchemaContext();
			setValidationResult(SchemaValidationStatus.INVALID, schemaContext.getValidationErrorMessages());
			throw e;
		} catch(JsonSchemaException e) {
			setValidationResult(SchemaValidationStatus.INVALID, null);
			throw e;
		} catch(Exception e) {
			setValidationResult(SchemaValidationStatus.INVALID, null);
			throw e;
		}
	}
	
	void setValidationResult(SchemaValidationStatus validationStatus, List<String> validationErrorMessages) {
		this.validationStatus = validationStatus;
		this.validationErrorMessages = validationErrorMessages;
	}
	
	public SchemaValidationStatus getValidationStatus() {
		return validationStatus;
	}
	
	public List<String> getValidationErrorMessages() {
		return validationErrorMessages;
	}
	
	public boolean hasValidationErrorMessages() {
		return validationErrorMessages != null;
	}
}
