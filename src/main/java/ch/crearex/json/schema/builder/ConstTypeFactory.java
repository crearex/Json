package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.TypeFactory;

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
