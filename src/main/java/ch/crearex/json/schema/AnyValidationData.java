package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public class AnyValidationData implements ValidationData {


	public ContainerType getNextObjectType(JsonSchemaContext context) {
		return ObjectType.EMTPY_OBJECT;
	}
	
	public ContainerType getNextArrayType(JsonSchemaContext context) {
		return ArrayType.EMTPTY_ARRAY;
	}
	
	public void validateObjectFinal(JsonSchemaContext context) {
	}

	public void validateArrayFinal(JsonSchemaContext context) {
	}
	
	public void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value) {
	}
	
	public void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value) {
	}

}
