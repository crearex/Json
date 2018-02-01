package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonObject;

public class NumberTypeBuilder implements TypeBuilder {

	private final BuilderContext context;
	
	public NumberTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	@Override
	public NumberType build(JsonObject typeDefinition) {
		NumberType type = new NumberType(
				typeDefinition.getString(SchemaConstants.TITLE_NAME, ""),
				typeDefinition.getString(SchemaConstants.DESCRIPTION_NAME, ""));
		
		if(typeDefinition.isFloatingpoint(SchemaConstants.MAXIMUM_CONSTRAINT)) {
			type.addConstraint(new DoubleMaximumConstraint(typeDefinition.getDouble(SchemaConstants.MAXIMUM_CONSTRAINT)));
		} else if(typeDefinition.isNumber(SchemaConstants.MAXIMUM_CONSTRAINT)) {
			type.addConstraint(new LongMaximumConstraint(typeDefinition.getLong(SchemaConstants.MAXIMUM_CONSTRAINT)));
		}
		
		if(typeDefinition.isFloatingpoint(SchemaConstants.MINIMUM_CONSTRAINT)) {
			type.addConstraint(new DoubleMinimumConstraint(typeDefinition.getDouble(SchemaConstants.MINIMUM_CONSTRAINT)));
		} else if(typeDefinition.isNumber(SchemaConstants.MINIMUM_CONSTRAINT)) {
			type.addConstraint(new LongMinimumConstraint(typeDefinition.getLong(SchemaConstants.MINIMUM_CONSTRAINT)));
		}
		
		return type;
	}

}
