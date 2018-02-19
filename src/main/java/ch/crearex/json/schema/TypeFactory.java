package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public interface TypeFactory {

	SchemaList createPossibleTypes(JsonObject typeDefinition);

}