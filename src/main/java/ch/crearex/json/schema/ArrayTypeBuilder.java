package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.constraints.MaxItemsConstraint;
import ch.crearex.json.schema.constraints.MinItemsConstraint;

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
		
		if(definition.hasProperty(SchemaConstants.MAX_ITEMS_CONSTRAINT)) {
			int maxItems = definition.getInteger(SchemaConstants.MAX_ITEMS_CONSTRAINT, -1);
			if(maxItems < 0) {
				throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! "+SchemaConstants.MAX_ITEMS_CONSTRAINT+" must be >= 0.");
			}
			type.addConstraint(new MaxItemsConstraint(maxItems));
		}
		
		if(definition.hasProperty(SchemaConstants.MIN_ITEMS_CONSTRAINT)) {
			int minItems = definition.getInteger(SchemaConstants.MIN_ITEMS_CONSTRAINT, -1);
			if(minItems < 0) {
				throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! "+SchemaConstants.MIN_ITEMS_CONSTRAINT+" must be >= 0.");
			}
			type.addConstraint(new MinItemsConstraint(minItems));
		}
		
		if(definition.hasProperty(SchemaConstants.UNIQUE_ITEMS_CONSTRAINT)) {
			boolean uniqueItems = definition.getBoolean(SchemaConstants.UNIQUE_ITEMS_CONSTRAINT, false);
			type.setUniqueItems(uniqueItems);
		}
		
		return type;
	}
}
