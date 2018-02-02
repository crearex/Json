package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class StringTypeBuilder implements TypeBuilder {

	private final BuilderContext context;
	
	public StringTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	@Override
	public StringType build(JsonObject typeDefinition) {
		StringType type = new StringType(
				typeDefinition.getString(SchemaConstants.TITLE_NAME, ""),
				typeDefinition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		
		if(typeDefinition.isString(SchemaConstants.REGEX_CONSTRAINT)) {
			type.addConstraint(new RegexConstraint(typeDefinition.getString(SchemaConstants.REGEX_CONSTRAINT)));
		}
		
		return type;
	}

}
