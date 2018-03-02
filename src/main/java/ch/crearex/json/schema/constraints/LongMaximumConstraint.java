package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class LongMaximumConstraint implements SimpleValueConstraint {

	private final long maximum;
	
	public LongMaximumConstraint(long maximum) {
		this.maximum = maximum;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() <= maximum) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " > " + maximum + ".");
	}

}
