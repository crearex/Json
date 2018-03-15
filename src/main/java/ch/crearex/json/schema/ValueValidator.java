package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public interface ValueValidator {
	ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value);
}