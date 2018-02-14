package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;

public class ObjectValidationData implements ValidationData {

	private String nextPropertyName = null;
	private HashSet<String> readPropertyNames;
	private final ObjectType type;

	ObjectValidationData(ObjectType type) {
		this.type = type;
		readPropertyNames = new HashSet<String>();
	}

	public HashSet<String> getReadPropertyNames() {
		return readPropertyNames;
	}

	@Override
	public String toString() {
		return type.toString();
	}

	public ContainerType getNextObjectType(JsonSchemaContext context) {
		SchemaType nextType = type.getPropertyType(context, nextPropertyName, JsonObject.class);
		if (nextType == null) {
			return ObjectType.EMTPY_OBJECT;
		}
		if (nextType instanceof ObjectType) {
			return (ObjectType) nextType;
		}
		if (nextType instanceof AnyType) {
			return (AnyType) nextType;
		}
		throw new JsonSchemaException("Invalid " + nextType.getName() + " type for '" + context.getPath()
				+ "'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}

	public ContainerType getNextArrayType(JsonSchemaContext context) {
		SchemaType nextType = type.getPropertyType(context, nextPropertyName, JsonArray.class);
		if (nextType == null) {
			return ArrayType.EMTPTY_ARRAY;
		}
		if (nextType instanceof ArrayType) {
			return (ArrayType) nextType;
		}
		if (nextType instanceof AnyType) {
			return (AnyType) nextType;
		}
		throw new JsonSchemaException("Invalid " + nextType.getName() + " type for '" + context.getPath()
				+ "'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}

	void addProperty(JsonSchemaContext context, String propertyName) {
		nextPropertyName = propertyName;
		if (readPropertyNames.contains(propertyName)) {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(),
					"Property '" + context.getPath().concat(propertyName) + "' already defined!"));
		}
		readPropertyNames.add(propertyName);
	}

	String getNextProperty() {
		return nextPropertyName;
	}

	public void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		type.validatePropertyValue(context, nextPropertyName, value);
	}

	public void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		SchemaType propertyType = type.getPropertyType(context, nextPropertyName, domTypeClass);
		if (propertyType instanceof ValueType) {
			((ValueType) propertyType).validate(context, nextPropertyName, value);
		}
	}

	public void validateObjectFinal(JsonSchemaContext context) {
		type.validate(context, this);
	}

	public void validateArrayFinal(JsonSchemaContext context) {
		type.validate(context, this);
	}

}
