package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;

class EnumTypeFactory implements TypeFactory {

	private final BuilderContext context;
	
	EnumTypeFactory(BuilderContext context) {
		this.context = context;
	}

	@Override
	public SchemaType[] createPossibleTypes(JsonObject typeDefinition) {
		JsonArray enumeration = typeDefinition.getArray(SchemaConstants.ENUM_NAME);
		if(enumeration == null) {
			throw new JsonSchemaException("Read enum declaration in '"+typeDefinition.getPath()+"' failed!");
		}
		return new SchemaType[] {createEnumType(enumeration)};
	}

	private SchemaType createEnumType(JsonArray enumeration) {
		EnumType enumType = new EnumType();
		for(JsonElement elem: enumeration) {
			if(!(elem instanceof JsonSimpleValue)) {
				continue;
			}
			enumType.add((JsonSimpleValue)elem);
		}
		return enumType;
	}

}
