package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class DoubleExclusiveMaximumConstraint implements SimpleValueConstraint {
	
	private final double maximum;
	
	public DoubleExclusiveMaximumConstraint(double maximum) {
		this.maximum = maximum;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() < maximum) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " >= " + maximum + ".");	
	}

}