package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SimpleValueConstraint;

public class LongMaximumConstraint implements SimpleValueConstraint {

	private final long maximum;
	
	public LongMaximumConstraint(long maximum) {
		this.maximum = maximum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() <= maximum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " > " + maximum + "."));
	}

}
