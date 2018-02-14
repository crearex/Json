package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonArray;

public interface ArrayConstraint {
	void validate(JsonSchemaContext context, JsonArray array);
}
