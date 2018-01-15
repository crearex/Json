package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.JsonNullValue;

public class ValidationData {

	private final SchemaType type;
	private Validator validator;
	
	private String nextPropertyName = null;
	private HashSet<String> readPropertyNames;
	
	ValidationData(SchemaType type) {
		this.type = type;
		if(type instanceof ObjectType) {
			ObjectType objectType = (ObjectType)type;
			validator = new ObjectTypeValidator(objectType);
			readPropertyNames = new HashSet<String>();
		} else if(type instanceof ArrayType) {
			ArrayType arrayType = (ArrayType)type;
			validator = new ArrayTypeValidator(arrayType);
		} else {
			throw new JsonSchemaException("Illegal validationtype: " + type.getClass().getSimpleName());
		}
	}
	@Override
	public String toString() {
		return type.toString();
	}
	
	SchemaType validateBeginObject(JsonSchemaContext context) {
		return validateType(context, JsonObject.class);
	}

	SchemaType validateBeginArray(JsonSchemaContext context) {
		return validateType(context, JsonArray.class);
	}

	protected void validateObjectFinal(JsonSchemaContext context) {
		ObjectType objectType = (ObjectType)type;
		for(String requiredProperty: objectType.getRequiredPropertyNames()) {
			if(!readPropertyNames.contains(requiredProperty)) {
				context.notifySchemaViolation("Required Property " + context.getPath().concat(requiredProperty) + " missing!");
			}
		}
	}

	void validateArrayFinal(JsonSchemaContext context) {
	}

	void addProperty(JsonSchemaContext context, String propertyName) {
		nextPropertyName = propertyName;
		if(readPropertyNames.contains(propertyName)) {
			context.notifySchemaViolation("Property "+context.getPath().concat(propertyName) +" already defined!");
		}
		readPropertyNames.add(propertyName);
	}

	void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		validateType(context, value.getClass());
	}
	
	private SchemaType validateType(JsonSchemaContext context, Class<?> domTypeClass) {
		if(isNullAllowed(context, domTypeClass)) {
			return SchemaType.ANY;
		}
		if(validator instanceof ObjectTypeValidator) {
			return validator.validatePropertyType(context, nextPropertyName, domTypeClass);
		} else if(validator instanceof ArrayTypeValidator) {
			return validator.validateArrayEntryType(context, domTypeClass);
		} else {
			throw new JsonSchemaException("Illegal validator type at "+context.getPath()+"!");
		}
	}

	private boolean isNullAllowed(JsonSchemaContext context, Class<?> domTypeClass) {
		return type.isNullable() && JsonNullValue.class.isAssignableFrom(domTypeClass);
	}
	
//	SchemaType getNextContainerType(JsonSchemaContext context) {
//		if(type instanceof ObjectType) {
//			return ((ObjectType)type).getPropertyType(nextPropertyName);
//		} else if(type instanceof ArrayType) {
//			return ((ArrayType)type).getItemType();
//		} else {
//			throw new JsonSchemaException("Illegal container type at "+context.getPath()+"!");
//		}
//	}

}
