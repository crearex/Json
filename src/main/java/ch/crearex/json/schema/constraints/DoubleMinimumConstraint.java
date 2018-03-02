package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class DoubleMinimumConstraint implements SimpleValueConstraint {

	private final double minimum;
	
	public DoubleMinimumConstraint(double minimum) {
		this.minimum = minimum;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() >= minimum) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " < " + minimum + ".");	
	}

}
