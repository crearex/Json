package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public interface SimpleValueConstraint {
	ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value);
}
