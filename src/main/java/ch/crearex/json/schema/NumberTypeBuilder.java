package ch.crearex.json.schema;

import org.hamcrest.TypeSafeDiagnosingMatcher;

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
		
		if(typeDefinition.isFloatingpoint(SchemaConstants.EXCLUSIVE_MAXIMUM_CONSTRAINT)) {
			type.addConstraint(new DoubleExclusiveMaximumConstraint(typeDefinition.getDouble(SchemaConstants.EXCLUSIVE_MAXIMUM_CONSTRAINT)));
		} else if(typeDefinition.isNumber(SchemaConstants.EXCLUSIVE_MAXIMUM_CONSTRAINT)) {
			type.addConstraint(new LongExclusiveMaximumConstraint(typeDefinition.getLong(SchemaConstants.EXCLUSIVE_MAXIMUM_CONSTRAINT)));
		}
		
		if(typeDefinition.isFloatingpoint(SchemaConstants.EXCLUSIVE_MINIMUM_CONSTRAINT)) {
			type.addConstraint(new DoubleExclusiveMinimumConstraint(typeDefinition.getDouble(SchemaConstants.EXCLUSIVE_MINIMUM_CONSTRAINT)));
		} else if(typeDefinition.isNumber(SchemaConstants.EXCLUSIVE_MINIMUM_CONSTRAINT)) {
			type.addConstraint(new LongExclusiveMinimumConstraint(typeDefinition.getLong(SchemaConstants.EXCLUSIVE_MINIMUM_CONSTRAINT)));
		}
		
		try {
			if(typeDefinition.isFloatingpoint(SchemaConstants.MULTIPLE_OF_CONSTRAINT)) {
				type.addConstraint(new DoubleMultipleOfConstraint(typeDefinition.getDouble(SchemaConstants.MULTIPLE_OF_CONSTRAINT)));
			} else if(typeDefinition.isNumber(SchemaConstants.MULTIPLE_OF_CONSTRAINT)) {
				type.addConstraint(new LongMultipleOfConstraint(typeDefinition.getLong(SchemaConstants.MULTIPLE_OF_CONSTRAINT)));
			}
		} catch(JsonSchemaException e) {
			throw new JsonSchemaException("Illegal Constraint at '" + typeDefinition.getPath() + "': " + e.getMessage(), e);
		}
		
		return type;
	}

}
