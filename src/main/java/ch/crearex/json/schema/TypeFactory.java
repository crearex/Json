package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;

public class TypeFactory {
	
	private final BuilderContext context;
	
	TypeFactory(BuilderContext context) {
		this.context = context;
	}
	
	SchemaType[] createType(JsonObject typeDefinition) {
		if(typeDefinition.isString(SchemaConstants.TYPE_NAME)) {
			String typeName = typeDefinition.getString(SchemaConstants.TYPE_NAME);
			SchemaType type = createType(typeName, typeDefinition);
			return new SchemaType[] {type};
		} else if(typeDefinition.isArray(SchemaConstants.TYPE_NAME)) {
			JsonArray array = typeDefinition.getArray(SchemaConstants.TYPE_NAME);
			SchemaType[] retArr = new SchemaType[array.size()];
			int index = 0;
			for(JsonElement elem : array) {
				SchemaType type = createType(elem.toString(), typeDefinition);
				retArr[index] = type;
				index++;
			}
			return retArr;
		}
		throw new JsonSchemaException("Illegal schema type declaration!");
	}
	
	private SchemaType createType(String typeName, JsonObject typeDefinition) {
		TypeBuilder builder = createBuilder(typeName);
		return builder.build(typeDefinition);
	}

	private TypeBuilder createBuilder(String typeName) {
		if(typeName == null) {
			typeName = "";
		}
		switch(typeName) {
			case SchemaConstants.OBJECT_TYPE: {
				return new ObjectTypeBuilder(context);
			}
			case SchemaConstants.ARRAY_TYPE: {
				return new ArrayTypeBuilder(context);
			}
			case SchemaConstants.STRING_TYPE: {
				return new StringTypeBuilder(context);
			}
			case SchemaConstants.NUMBER_TYPE: {
				return new NumberTypeBuilder(context);
			}
			case SchemaConstants.BOOLEAN_TYPE: {
				return new BooleanTypeBuilder(context);
			}
			case "": {
				return new AnyBuilder(context);
			}
		}
		throw new JsonSchemaException("Type '"+typeName+"' unknown!");
	}

}
