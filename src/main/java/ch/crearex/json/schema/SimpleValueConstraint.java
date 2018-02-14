package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public interface SimpleValueConstraint {
	void validate(JsonSchemaContext context, JsonSimpleValue value);
}
