package ch.crearex.json.schema;

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
		
		type.addItemType(context.getTypeFactory().createType(items));
		return type;
	}
}
