package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonObject;

public class ArrayValidationData extends ValidationData {

	private int nextArrayIndex = 0;
	private final ArrayType actualContainerType;
	
	ArrayValidationData(ArrayType type) {
		super(type);
		this.actualContainerType = type;
	}
	
	ObjectType getNextObjectType(JsonSchemaContext context) {
		SchemaType nextType = actualContainerType.getEntryType(context, nextArrayIndex, ArrayType.class);
		
		if(nextType == null) {
			return ObjectType.EMTPY_OBJECT;
		}
		
		if(nextType instanceof ObjectType) {
			return (ObjectType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}
	
	ContainerType getNextArrayType(JsonSchemaContext context) {
		SchemaType nextType = actualContainerType.getEntryType(context, nextArrayIndex, ArrayType.class);
		
		if(nextType == null) {
			return ArrayType.EMTPTY_ARRAY;
		}
		
		if(nextType instanceof ArrayType) {
			return (ArrayType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE + ".");
	}
	
	void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		actualContainerType.validateEntryValue(context, nextArrayIndex, value);
	}
	
	void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		Class<?> domTypeClass = value.getClass();
		SchemaType type = actualContainerType.getEntryType(context, nextArrayIndex, domTypeClass);
		if(type instanceof ValueType) {
			((ValueType)type).validate(context, "", value);
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

}
