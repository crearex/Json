package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.constraints.MaxLengthConstraint;
import ch.crearex.json.schema.constraints.MinLengthConstraint;
import ch.crearex.json.schema.constraints.RegexConstraint;

public class StringTypeBuilder implements TypeBuilder {

	
	public StringTypeBuilder() {
	}

	@Override
	public StringType build(JsonObject typeDefinition) {
		StringType type = new StringType(
				typeDefinition.getString(SchemaConstants.TITLE_NAME, ""),
				typeDefinition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		
		if(typeDefinition.isString(SchemaConstants.REGEX_CONSTRAINT)) {
			type.addConstraint(new RegexConstraint(typeDefinition.getString(SchemaConstants.REGEX_CONSTRAINT)));
		}
		if(typeDefinition.isString(SchemaConstants.PATTERN_CONSTRAINT)) {
			type.addConstraint(new RegexConstraint(typeDefinition.getString(SchemaConstants.PATTERN_CONSTRAINT)));
		}
		if(typeDefinition.isNumber(SchemaConstants.MAX_LENGTH_CONSTRAINT)) {
			type.addConstraint(new MaxLengthConstraint(typeDefinition.getInteger(SchemaConstants.MAX_LENGTH_CONSTRAINT)));
		}
		if(typeDefinition.isNumber(SchemaConstants.MIN_LENGTH_CONSTRAINT)) {
			type.addConstraint(new MinLengthConstraint(typeDefinition.getInteger(SchemaConstants.MIN_LENGTH_CONSTRAINT)));
		}
		
		return type;
	}

}
