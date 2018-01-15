package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class AnyBuilder implements TypeBuilder {

	public AnyBuilder(BuilderContext context) {
	}

	@Override
	public SchemaType build(JsonObject objectDefinition) {
		return SchemaType.ANY;
	}

}
