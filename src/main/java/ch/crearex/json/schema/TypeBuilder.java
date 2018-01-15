package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public interface TypeBuilder {

	SchemaType build(JsonObject definition);

}