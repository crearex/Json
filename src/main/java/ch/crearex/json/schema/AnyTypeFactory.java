package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class AnyTypeFactory implements TypeFactory {

	private final SchemaList MATCH_ANY = new SchemaList(new SchemaType[] { SchemaType.ANY_NULLABLE});
	
	public AnyTypeFactory(BuilderContext builderContext) {
	}

	@Override
	public SchemaList createPossibleTypes(JsonObject typeDefinition) {
		return MATCH_ANY;
	}

}
