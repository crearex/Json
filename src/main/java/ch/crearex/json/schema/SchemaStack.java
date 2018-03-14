package ch.crearex.json.schema;

import java.net.URL;
import java.util.LinkedList;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.JsonSimpleValue;

public class SchemaStack {

	private final LinkedList<ValidationData> stack = new LinkedList<ValidationData>();
	private final JsonSchemaContext schemaContext;
	private boolean beginRootContainer = true;
	
	SchemaStack(ContainerType schemaRootType, JsonSchemaCallback schemaCallback, URL jsonSchemaOriginUrl) {
		this.schemaContext = new JsonSchemaContextImpl(schemaCallback, jsonSchemaOriginUrl);
		stack.addFirst(createValidationData(schemaRootType));
	}
	
	private ValidationData createValidationData(ContainerType container) {
		// Specialized types comes first!!!
		// An AndSchema is also an ObjectSchema...
		if(container instanceof AndSchema) {
			AndSchema and = (AndSchema)container;
			switch(and.getFirstChildTypeName()) {
			case SchemaConstants.OBJECT_TYPE: {
				return new ObjectValidationData(and.getObjectAccess());
			}
			case SchemaConstants.ARRAY_TYPE: {
				return new ArrayValidationData(and.getArrayAccess());
			}
			}
		}
		if(container instanceof ObjectType) {
			return new ObjectValidationData((ObjectType)container);
		}
		if(container instanceof ArrayType) {
			return new ArrayValidationData((ArrayType)container);
		}
		if(container instanceof AnyType) {
			return new AnyValidationData();
		}
		throw new JsonSchemaException("Unsupported Container Type: " + container.getClass().getSimpleName());
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
			ContainerType nextType = validatonData.getNextObjectType(schemaContext);
			stack.addFirst(createValidationData(nextType));
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
			// TODO mn: ist der reset des next array index nötig?
			// validatonData.setNextArrayIndex(0);
			ContainerType nextType = validatonData.getNextArrayType(schemaContext);
			ValidationData validationData = createValidationData(nextType);
			stack.addFirst(validationData);
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
			if(validationData instanceof ObjectValidationData) {
				((ObjectValidationData)validationData).addProperty(schemaContext, propertyName);
			}
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Add property '"+context.getPath()+"' to validation data failed: " + e.getMessage(), e);
		}
	}

	void validateSimpleValue(JsonContext context, JsonSimpleValue value) {
		schemaContext.setAdaptedContext(context);
		ValidationData validationData = stack.getFirst();
		try {
			validationData.validateSimpleType(schemaContext, value);
			validationData.validateSimpleValue(schemaContext, value);
		} catch(JsonSchemaException e) {
			throw e;
		} catch(Exception e) {
			throw new JsonSchemaException("Validate '"+context.getPath()+"' failed: " + e.getMessage(), e);
		} finally {
			if(validationData instanceof ArrayValidationData) {
				((ArrayValidationData)validationData).incArrayIndex();
			}
		}
	}

}
