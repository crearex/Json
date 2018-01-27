package ch.crearex.json.schema;

import java.net.URL;
import java.util.LinkedList;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.JsonSimpleValue;

public class SchemaStack {

	private final SchemaType schemaRootType;
	private final LinkedList<ValidationData> stack = new LinkedList<ValidationData>();
	private final JsonSchemaContext schemaContext;
	private boolean beginRootContainer = true;
	
	SchemaStack(SchemaType schemaRootType, JsonSchemaCallback schemaCallback, URL jsonSchemaOriginUrl) {
		this.schemaRootType = schemaRootType;
		this.schemaContext = new JsonSchemaContextImpl(schemaCallback, jsonSchemaOriginUrl);
		stack.addFirst(new ValidationData(schemaRootType));
	}
	
	public JsonSchemaContext getSchemaContext() {
		return schemaContext;
	}

	void beginObject(JsonContext context) {
		schemaContext.setAdaptedContext(context);
		try {
			if(beginRootContainer) {
				return;
			}
			ValidationData validatonData = stack.getFirst();
			ObjectType nextType = validatonData.getNextPropertiesObjectType(schemaContext);
			stack.addFirst(new ValidationData(nextType));
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate object begin failed: " + e.getMessage(), e);
		} finally {
			beginRootContainer = false;
		}
	}

	void beginArray(JsonContext context) {
		schemaContext.setAdaptedContext(context);
		try {
			if(beginRootContainer) {
				return;
			}
			ValidationData validatonData = stack.getFirst();
			SchemaType nextType = validatonData.validateBeginArray(schemaContext);
			//SchemaType nextType = validatonData.getNextContainerType(schemaContext);
			stack.addFirst(new ValidationData(nextType));
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate array begin failed: " + e.getMessage(), e);
		} finally {
			beginRootContainer = false;
		}
	}

	void endObject(JsonContext context) {
		schemaContext.setAdaptedContext(context);
		try {
			ValidationData validationData = stack.removeFirst();
			validationData.validateObjectFinal(schemaContext);
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate object failed: " + e.getMessage(), e);
		}
	}

	void endArray(JsonContext context) {
		schemaContext.setAdaptedContext(context);
		try {
			ValidationData validationData = stack.removeFirst();
			validationData.validateArrayFinal(schemaContext);
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate array failed: " + e.getMessage(), e);
		}
	}

	void setNextProperty(JsonContext context, String propertyName) {
		schemaContext.setAdaptedContext(context);
		try {
			ValidationData validationData = stack.getFirst();
			validationData.addProperty(schemaContext, propertyName);
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate array failed: " + e.getMessage(), e);
		}
	}

	void validateSimpleValue(JsonContext context, JsonSimpleValue value) {
		schemaContext.setAdaptedContext(context);
		try {
			ValidationData validationData = stack.getFirst();
			validationData.validateSimpleType(schemaContext, value);
			validationData.validateSimpleValue(schemaContext, value);
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate array failed: " + e.getMessage(), e);
		}
	}

}
