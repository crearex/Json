package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.builder.ContainerType;

public interface ValidationData {
	ContainerType getNextObjectType(JsonSchemaContext context);
	ContainerType getNextArrayType(JsonSchemaContext context);
	void validateObjectFinal(JsonSchemaContext context);
	void validateArrayFinal(JsonSchemaContext context);
	void validateSimpleValue(JsonSchemaContext context, JsonSimpleValue value);
	void validateSimpleType(JsonSchemaContext context, JsonSimpleValue value);
}
