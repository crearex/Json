package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class AnyTypeFactory implements TypeFactory {

	private final SchemaType[] MATCH_ANY = new SchemaType[] { SchemaType.ANY_NULLABLE};
	
	public AnyTypeFactory(BuilderContext builderContext) {
	}

	@Override
	public SchemaType[] createPossibleTypes(JsonObject typeDefinition) {
		return MATCH_ANY;
	}

}
