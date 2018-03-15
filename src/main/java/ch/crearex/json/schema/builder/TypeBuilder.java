package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.SchemaType;

interface TypeBuilder {
	SchemaType build(JsonObject definition);
}