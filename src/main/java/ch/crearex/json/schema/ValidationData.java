package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.JsonNullValue;

public class ValidationData {

	private final SchemaType actualContainerType;
	
	private String nextPropertyName = null;
	private HashSet<String> readPropertyNames;
	
	ValidationData(SchemaType type) {
		this.actualContainerType = type;
		if(type instanceof ObjectType) {
			readPropertyNames = new HashSet<String>();
		} else if(type instanceof ArrayType) {
			// nothing to do
		} else {
			throw new JsonSchemaException("Unexpected validation datatype: " + type.getClass().getSimpleName());
		}
	}
	
	@Override
	public String toString() {
		return actualContainerType.toString();
	}
	
	ObjectType getNextPropertiesObjectType(JsonSchemaContext context) {
		if(actualContainerType instanceof ObjectType) {
			SchemaType nextType = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, JsonObject.class);
			if(nextType instanceof ObjectType) {
				return (ObjectType)nextType;
			}
			throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE);
		} 
		throw new JsonSchemaException("Unexpected validation type '"+actualContainerType.getName()+"' for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE);
	}

	ArrayType validateBeginArray(JsonSchemaContext context) {
		if(actualContainerType instanceof ArrayType) {
			SchemaType nextType = ((ArrayType)actualContainerType).getEntryType(context, ArrayType.class);
			if(nextType instanceof ArrayType) {
				return (ArrayType)nextType;
			}
			throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE);
		} 
		throw new JsonSchemaException("Unexpected validation type "+actualContainerType.getName()+" for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE);
	}

	protected void validateObjectFinal(JsonSchemaContext context) {
		ObjectType objectType = (ObjectType)actualContainerType;
		for(String requiredProperty: objectType.getRequiredPropertyNames()) {
			if(!readPropertyNames.contains(requiredProperty)) {
				context.notifySchemaViolation("Required Property '" + context.getPath().concat(requiredProperty) + "' missing!");
			}
		}
	}

	void validateArrayFinal(JsonSchemaContext context) {
	}

	void addProperty(JsonSchemaContext context, String propertyName) {
		nextPropertyName = propertyName;
		if(readPropertyNames.contains(propertyName)) {
			context.notifySchemaViolation("Property '"+context.getPath().concat(propertyName) +"' already defined!");
		}
		readPropertyNames.add(propertyName);
	}
	
	String getNextProperty() {
		return nextPropertyName;
	}

	void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		if(actualContainerType instanceof ObjectType) {
			((ObjectType)actualContainerType).validatePropertyValue(context, nextPropertyName, value);
		} else if(actualContainerType instanceof ArrayType) {
			((ArrayType)actualContainerType).validateEntryValue(context, value);
		} else {
			throw new JsonSchemaException("Unexpected schema type type at '"+context.getPath()+"'!");
		}
	}
	
	SchemaType validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		// TODO null allowed heisst nicht ANY...
		if(isNullAllowed(context, domTypeClass)) {
			return SchemaType.ANY;
		}
		
		if(actualContainerType instanceof ObjectType) {
			return ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, domTypeClass);
		} else if(actualContainerType instanceof ArrayType) {
			return ((ArrayType)actualContainerType).getEntryType(context, domTypeClass);
		} else {
			throw new JsonSchemaException("Unexpected schema type type at '"+context.getPath()+"'!");
		}
	}

	private boolean isNullAllowed(JsonSchemaContext context, Class<?> domTypeClass) {
		return actualContainerType.isNullable() && JsonNullValue.class.isAssignableFrom(domTypeClass);
	}


}
