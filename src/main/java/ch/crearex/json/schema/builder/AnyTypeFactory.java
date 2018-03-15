package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.TypeFactory;

public class AnyTypeFactory implements TypeFactory {

	private final SchemaList MATCH_ANY = new SchemaList(new SchemaType[] { AnyType.ANY});
	
	public AnyTypeFactory(BuilderContext builderContext) {
	}

	@Override
	public SchemaList createPossibleTypes(JsonObject typeDefinition) {
		return MATCH_ANY;
	}

}
