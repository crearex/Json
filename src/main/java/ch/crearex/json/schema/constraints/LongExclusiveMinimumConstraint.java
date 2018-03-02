package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class LongExclusiveMinimumConstraint implements SimpleValueConstraint {

	private final long minimum;
	
	public LongExclusiveMinimumConstraint(long minimum) {
		this.minimum = minimum;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() > minimum) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " <= " + minimum + ".");	
	}

}
