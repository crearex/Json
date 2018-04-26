package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.builder.AndSchema;
import ch.crearex.json.schema.builder.ArrayType;
import ch.crearex.json.schema.builder.ContainerType;
import ch.crearex.json.schema.builder.ObjectType;
import ch.crearex.json.schema.builder.ValueType;

public class ArrayValidationData implements ValidationData {

	private int nextArrayIndex = 0;
	private final ArrayType type;
	
	ArrayValidationData(ArrayType type) {
		this.type = type;
	}
	
	public ContainerType getNextObjectType(JsonSchemaContext context) {
		SchemaType nextType = type.getEntryType(context, nextArrayIndex, SchemaConstants.ARRAY_TYPE);
		
		if(nextType == null) {
			return ObjectType.EMTPY_OBJECT;
		}
		if (nextType instanceof AndSchema) {
			return (AndSchema)nextType;
		}
		if(nextType instanceof ObjectType) {
			return (ObjectType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getTypeName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}
	
	public ContainerType getNextArrayType(JsonSchemaContext context) {
		SchemaType nextType = type.getEntryType(context, nextArrayIndex, SchemaConstants.ARRAY_TYPE);
		
		if(nextType == null) {
			return ArrayType.EMTPTY_ARRAY;
		}
		if (nextType instanceof AndSchema) {
			return (AndSchema)nextType;
		}
		if(nextType instanceof ArrayType) {
			return (ArrayType)nextType;
		}
		
		throw new JsonSchemaException("Invalid "+nextType.getTypeName()+" type for '"+context.getPath()+"'! Expected: " + SchemaConstants.ARRAY_TYPE + ".");
	}
	
	public void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
		type.validateEntryValue(context, nextArrayIndex, value);
	}
	
	public void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
		SchemaType entryType = type.getEntryType(context, nextArrayIndex, value.getTypeName());
		if(entryType instanceof ValueType) {
			((ValueValidator)entryType).validate(context, value);
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
		ValidationResult result = type.validate(context, this);
		if(result != ValidationResult.OK) {
			context.notifySchemaViolation(new JsonSchemaValidationException(result));
		}
	}

	public void validateArrayFinal(JsonSchemaContext context) {
		ValidationResult result = type.validate(context, this);
		if(result != ValidationResult.OK) {
			context.notifySchemaViolation(new JsonSchemaValidationException(result));
		}
	}
	
	@Override
	public String toString() {
		return type.toString();
	}

}
