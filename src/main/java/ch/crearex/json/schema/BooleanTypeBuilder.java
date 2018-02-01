package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class BooleanTypeBuilder implements TypeBuilder {

	private final BuilderContext context;
	
	public BooleanTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	@Override
	public BooleanType build(JsonObject definition) {
		BooleanType type = new BooleanType(
				definition.getString(SchemaConstants.TITLE_NAME, ""),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		return type;
	}

}
