package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.JsonNullValue;

public class ValidationData {

	private final ContainerType actualContainerType;
	
	private String nextPropertyName = null;
	private HashSet<String> readPropertyNames;
	
	ValidationData(ContainerType type) {
		this.actualContainerType = type;
		if(type instanceof ObjectType) {
			readPropertyNames = new HashSet<String>();
		}
	}
	
	@Override
	public String toString() {
		return actualContainerType.toString();
	}
	
	ObjectType getNextObjectType(JsonSchemaContext context) {
		if(actualContainerType instanceof ObjectType) {
			SchemaType nextType = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, JsonObject.class);
			if(nextType == null) {
				return ObjectType.EMTPY_OBJECT;
			}
			if(nextType instanceof ObjectType) {
				return (ObjectType)nextType;
			}
			throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE);
		}
		
		
		// actualContainerType is an ArrayType
		SchemaType nextType = ((ArrayType)actualContainerType).getEntryType(context, ArrayType.class);
		if(nextType == null) {
			return ObjectType.EMTPY_OBJECT;
		}
		if(nextType instanceof ObjectType) {
			return (ObjectType)nextType;
		}

		throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE);
	}

	ArrayType getNextArrayType(JsonSchemaContext context) {
		if(actualContainerType instanceof ObjectType) {
			SchemaType nextType = ((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, JsonArray.class);
			if(nextType == null) {
				return ArrayType.EMTPTY_ARRAY;
			}
			if(nextType instanceof ArrayType) {
				return (ArrayType)nextType;
			}
			throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE);
		} 
		
		// actualContainerType it an array
		SchemaType nextType = ((ArrayType)actualContainerType).getEntryType(context, ArrayType.class);
		if(nextType == null) {
			return ArrayType.EMTPTY_ARRAY;
		}
		if(nextType instanceof ArrayType) {
			return (ArrayType)nextType;
		}
		
		throw new JsonSchemaException("Unexpected "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE);
	}

	void validateObjectFinal(JsonSchemaContext context) {
		ObjectType objectType = (ObjectType)actualContainerType;
		for(String requiredProperty: objectType.getRequiredPropertyNames()) {
			if(!readPropertyNames.contains(requiredProperty)) {
				context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Required Property '" + context.getPath().concat(requiredProperty) + "' missing!"));
			}
		}
	}

	void validateArrayFinal(JsonSchemaContext context) {
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
		} else if(actualContainerType instanceof ArrayType) {
			((ArrayType)actualContainerType).validateEntryValue(context, value);
		} else {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected schema type type at '"+context.getPath()+"'!"));
		}
	}
	
	void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		
		if(actualContainerType instanceof ObjectType) {
			((ObjectType)actualContainerType).getPropertyType(context, nextPropertyName, domTypeClass);
// The nullable check is already done in getEntryType()!	
//			if(propertyType != null) {
//				if(!propertyType.isNullable() && value.isNull()) {
//					context.notifySchemaViolation("Property value '"+context.getPath()+"' must not be " + SchemaConstants.NULL_TYPE + "!");
//				}
//			}
		} else if(actualContainerType instanceof ArrayType) {
			((ArrayType)actualContainerType).getEntryType(context, domTypeClass);
// The nullable check is already done in getEntryType()!			
//			if(entryType != null) {
//				if(!entryType.isNullable() && value.isNull()) {
//					context.notifySchemaViolation("Array value of '"+context.getPath()+"' must not be " + SchemaConstants.NULL_TYPE + "!");
//				}
//			}
		} else {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected schema type type at '"+context.getPath()+"'!"));
		}
	}

}
