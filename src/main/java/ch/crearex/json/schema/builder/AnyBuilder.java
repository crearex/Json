package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;

public class AnyBuilder implements TypeBuilder {

	public AnyBuilder() {
	}

	@Override
	public AnyType build(JsonObject objectDefinition) {
		return AnyType.ANY;
	}

}
