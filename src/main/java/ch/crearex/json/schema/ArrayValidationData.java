package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;

public class ArrayValidationData implements ValidationData {

	private int nextArrayIndex = 0;
	private final ArrayType type;
	
	ArrayValidationData(ArrayType type) {
		this.type = type;
	}
	
	public ObjectType getNextObjectType(JsonSchemaContext context) {
		SchemaType nextType = type.getEntryType(context, nextArrayIndex, ArrayType.class);
		
		if(nextType == null) {
			return ObjectType.EMTPY_OBJECT;
		}
		
		if(nextType instanceof ObjectType) {
			return (ObjectType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}
	
	public ContainerType getNextArrayType(JsonSchemaContext context) {
		SchemaType nextType = type.getEntryType(context, nextArrayIndex, ArrayType.class);
		
		if(nextType == null) {
			return ArrayType.EMTPTY_ARRAY;
		}
		
		if(nextType instanceof ArrayType) {
			return (ArrayType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE + ".");
	}
	
	public void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		type.validateEntryValue(context, nextArrayIndex, value);
	}
	
	public void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		SchemaType entryType = type.getEntryType(context, nextArrayIndex, domTypeClass);
		if(entryType instanceof ValueType) {
			((ValueType)entryType).validate(context, "", value);
		}
	}
	
	void setNextArrayIndex(int nextArrayIndex) {
		this.nextArrayIndex = nextArrayIndex;
	}
	
	public int getNextArrayIndex() {
		return nextArrayIndex;
	}

	public void incArrayIndex() {
		nextArrayIndex++;	
	}
	
	public void validateObjectFinal(JsonSchemaContext context) {
		type.validate(context, this);
	}

	public void validateArrayFinal(JsonSchemaContext context) {
		type.validate(context, this);
	}
	
	@Override
	public String toString() {
		return type.toString();
	}

}
