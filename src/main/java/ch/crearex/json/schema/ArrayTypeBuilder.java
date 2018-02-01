package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;

public class ArrayTypeBuilder implements TypeBuilder {
	
	private final BuilderContext context;
	
	public ArrayTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	public ArrayType build(JsonObject definition) {
		ArrayType type = new ArrayType(
				definition.getString(SchemaConstants.TITLE_NAME, ""),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		
		JsonObject items = definition.getObject(SchemaConstants.ITEMS_NAME);
		if(items != null) {
			SchemaType[] arrayTypes = context.getTypeFactory(items).createPossibleTypes(items);
			type.addItemTypes(arrayTypes);
		} else {
			JsonArray itemsArray = definition.getArray(SchemaConstants.ITEMS_NAME);
			if(itemsArray == null) {
				throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! Expected:["+SchemaConstants.OBJECT_TYPE+", "+SchemaConstants.ARRAY_TYPE+"].");
			}
			SchemaType[] arrayTypes = new SchemaType[itemsArray.size()];
			int index = 0;
			for(JsonElement elem: itemsArray) {
				if(elem instanceof JsonObject) {
					JsonObject entrySchema = (JsonObject)elem;
					SchemaType[] entrySchemaTypes = context.getTypeFactory(entrySchema).createPossibleTypes(entrySchema);
					arrayTypes[index] = entrySchemaTypes[0];
				}
				index++;
			}
			
			type.addItemTypes(arrayTypes);
		}
		type.setNullable(SchemaUtil.isNullableType(definition));
		return type;
	}
}
