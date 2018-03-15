package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.constraints.DoubleExclusiveMaximumConstraint;
import ch.crearex.json.schema.constraints.DoubleExclusiveMinimumConstraint;
import ch.crearex.json.schema.constraints.DoubleMaximumConstraint;
import ch.crearex.json.schema.constraints.DoubleMinimumConstraint;
import ch.crearex.json.schema.constraints.DoubleMultipleOfConstraint;
import ch.crearex.json.schema.constraints.LongExclusiveMaximumConstraint;
import ch.crearex.json.schema.constraints.LongExclusiveMinimumConstraint;
import ch.crearex.json.schema.constraints.LongMaximumConstraint;
import ch.crearex.json.schema.constraints.LongMinimumConstraint;
import ch.crearex.json.schema.constraints.LongMultipleOfConstraint;

public class NumberTypeBuilder implements TypeBuilder {

	public NumberTypeBuilder() {
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
