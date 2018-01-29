package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public interface TypeFactory {

	SchemaType[] createPossibleTypes(JsonObject typeDefinition);

}