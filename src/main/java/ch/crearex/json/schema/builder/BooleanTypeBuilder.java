package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.SchemaConstants;

public class BooleanTypeBuilder implements TypeBuilder {

	
	public BooleanTypeBuilder() {
	}

	@Override
	public BooleanType build(JsonObject definition) {
		BooleanType type = new BooleanType(
				definition.getString(SchemaConstants.TITLE_NAME, ""),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		return type;
	}

}
