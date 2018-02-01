package ch.crearex.json.schema;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;

public interface Constraint {
	void validate(JsonSchemaContext context, JsonSimpleValue value);
}
