package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;

public class ConstTypeFactory implements TypeFactory {

	public ConstTypeFactory(BuilderContext builderContext) {
	}

	@Override
	public SchemaList createPossibleTypes(JsonObject typeDefinition) {
		JsonElement constant = typeDefinition.getProperty(SchemaConstants.CONST_NAME);
		if(constant == null) {
			throw new JsonSchemaException("Read const declaration in '"+typeDefinition.getPath()+"' failed!");
		}
		return new SchemaList(new SchemaType[] {createConstType(constant)});
	}

	private SchemaType createConstType(JsonElement constant) {
		return new ConstType(constant);
	}

}
