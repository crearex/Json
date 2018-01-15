package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class NumberTypeBuilder implements TypeBuilder {

	private final BuilderContext context;
	
	public NumberTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	@Override
	public NumberType build(JsonObject definition) {
		NumberType type = new NumberType(
				definition.getString(SchemaConstants.TITLE_NAME, ""),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		return type;
	}

}
