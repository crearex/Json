package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;

public class ValidationData {

	private final ContainerType actualContainerType;
	
	
	private String nextPropertyName = null;
	private HashSet<String> readPropertyNames;
	
	ValidationData(ContainerType type) {
		this.actualContainerType = type;
		if((type instanceof ObjectType) || (type instanceof AnyType)) {
			readPropertyNames = new HashSet<String>();
		}
	}
	
	public ContainerType getContainerType() {
		return actualContainerType;
	}
	
	public HashSet<String> getReadPropertyNames() {
		return readPropertyNames;
	}
	
	@Override
	public String toString() {
		return actualContainerType.toString();
	}
	
	ContainerType getNextObjectType(JsonSchemaContext context) {
		if(actualContainerType instanceof ObjectType) {
			SchemaType nextType = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, JsonObject.class);
			if(nextType == null) {
				return ObjectType.EMTPY_OBJECT;
			}
			if(nextType instanceof ObjectType) {
				return (ObjectType)nextType;
			}
			if(nextType instanceof AnyType) {
				return (AnyType)nextType;
			}
			throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
		}
		
		if(actualContainerType instanceof AnyType) {
			return ObjectType.EMTPY_OBJECT;
		}
		

		throw new JsonSchemaException("Invalid type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}

	ContainerType getNextArrayType(JsonSchemaContext context) {
		if(actualContainerType instanceof ObjectType) {
			SchemaType nextType = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, JsonArray.class);
			if(nextType == null) {
				return ArrayType.EMTPTY_ARRAY;
			}
			if(nextType instanceof ArrayType) {
				return (ArrayType)nextType;
			}
			if(nextType instanceof AnyType) {
				return (AnyType)nextType;
			}
			throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
		} 
		
		throw new JsonSchemaException("Invalid type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE + ".");
	}

	void validateObjectFinal(JsonSchemaContext context) {
		if(actualContainerType instanceof AnyType) {
			return;
		}
		actualContainerType.validate(context, this);
	}

	void validateArrayFinal(JsonSchemaContext context) {
		if(actualContainerType instanceof AnyType) {
			return;
		}
		ArrayType arrayType = (ArrayType)actualContainerType;
		arrayType.validate(context, this);
	}

	void addProperty(JsonSchemaContext context, String propertyName) {
		nextPropertyName = propertyName;
		if(readPropertyNames.contains(propertyName)) {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Property '"+context.getPath().concat(propertyName) +"' already defined!"));
		}
		readPropertyNames.add(propertyName);
	}
	
	String getNextProperty() {
		return nextPropertyName;
	}

	void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		if(actualContainerType instanceof ObjectType) {
			((ObjectType)actualContainerType).validatePropertyValue(context, nextPropertyName, value);
		} else if(actualContainerType instanceof AnyType) {
			// do nothing
		} else {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected schema type at '"+context.getPath()+"'!"));
		}
	}
	
	void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		
		if(actualContainerType instanceof ObjectType) {
			SchemaType type = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, domTypeClass);
			if(type instanceof ValueType) {
				((ValueType)type).validate(context, nextPropertyName, value);
			}
			// The nullable check is already done in getEntryType()!	
//			if(propertyType != null) {
//				if(!propertyType.isNullable() && value.isNull()) {
//					context.notifySchemaViolation("Property value '"+context.getPath()+"' must not be " + SchemaConstants.NULL_TYPE + "!");
//				}
//			}
		} else if(actualContainerType instanceof AnyType) {
			// do nothing
		} else {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected schema type at '"+context.getPath()+"'!"));
		}
	}

}
