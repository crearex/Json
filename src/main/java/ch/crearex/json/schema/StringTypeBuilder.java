package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class StringTypeBuilder implements TypeBuilder {

	private final BuilderContext context;
	
	public StringTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	@Override
	public StringType build(JsonObject definition) {
		StringType type = new StringType(
				definition.getString(SchemaConstants.TITLE_NAME, ""),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		return type;
	}

}
