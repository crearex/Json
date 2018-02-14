package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SimpleValueConstraint;

public class LongExclusiveMinimumConstraint implements SimpleValueConstraint {

	private final long minimum;
	
	public LongExclusiveMinimumConstraint(long minimum) {
		this.minimum = minimum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() > minimum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " <= " + minimum + "."));	
	}

}
