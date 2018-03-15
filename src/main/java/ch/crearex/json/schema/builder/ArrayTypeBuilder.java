package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
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
			readSingleTypeforAllEntries(type, items);
		} else {
			readIndividualItemTypes(definition, type);
		}
		type.setNullable(SchemaUtil.isNullableType(definition));
		
		if(definition.hasProperty(SchemaConstants.MAX_ITEMS_CONSTRAINT)) {
			readMaxItemsConstraint(definition, type);
		}
		
		if(definition.hasProperty(SchemaConstants.MIN_ITEMS_CONSTRAINT)) {
			readMinItemsConstraint(definition, type);
		}
		
		if(definition.hasProperty(SchemaConstants.UNIQUE_ITEMS_CONSTRAINT)) {
			readUniqueItemsConstraint(definition, type);
		}
		
		return type;
	}

	private void readUniqueItemsConstraint(JsonObject definition, ArrayType type) {
		boolean uniqueItems = definition.getBoolean(SchemaConstants.UNIQUE_ITEMS_CONSTRAINT, false);
		type.setUniqueItems(uniqueItems);
	}

	private void readMinItemsConstraint(JsonObject definition, ArrayType type) {
		int minItems = definition.getInteger(SchemaConstants.MIN_ITEMS_CONSTRAINT, -1);
		if(minItems < 0) {
			throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! "+SchemaConstants.MIN_ITEMS_CONSTRAINT+" must be >= 0.");
		}
		type.addConstraint(new MinItemsConstraint(minItems));
	}

	private void readMaxItemsConstraint(JsonObject definition, ArrayType type) {
		int maxItems = definition.getInteger(SchemaConstants.MAX_ITEMS_CONSTRAINT, -1);
		if(maxItems < 0) {
			throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! "+SchemaConstants.MAX_ITEMS_CONSTRAINT+" must be >= 0.");
		}
		type.addConstraint(new MaxItemsConstraint(maxItems));
	}

	private void readIndividualItemTypes(JsonObject definition, ArrayType type) {
		JsonArray itemsArray = definition.getArray(SchemaConstants.ITEMS_NAME);
		if(itemsArray == null) {
			throw new JsonSchemaException("Illegal JSON Schema definition at '" + definition.getPath() + "'! Expected:["+SchemaConstants.OBJECT_TYPE+", "+SchemaConstants.ARRAY_TYPE+"].");
		}
		SchemaType[] arrayTypes = new SchemaType[itemsArray.size()];
		int index = 0;
		for(JsonElement elem: itemsArray) {
			if(elem instanceof JsonObject) {
				JsonObject entrySchema = (JsonObject)elem;
				SchemaList entrySchemaList = context.getTypeFactory(entrySchema).createPossibleTypes(entrySchema);
				arrayTypes[index] = entrySchemaList.getFirst();
			}
			index++;
		}
		
		type.addItemTypes(new SchemaList(arrayTypes));
	}

	private void readSingleTypeforAllEntries(ArrayType type, JsonObject items) {
		SchemaList arraySchemaList = context.getTypeFactory(items).createPossibleTypes(items);
		type.addItemTypes(arraySchemaList);
	}
}
