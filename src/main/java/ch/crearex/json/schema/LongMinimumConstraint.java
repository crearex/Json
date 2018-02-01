package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public class LongMinimumConstraint implements Constraint {

	private final long minimum;
	
	public LongMinimumConstraint(long minimum) {
		this.minimum = minimum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() >= minimum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " < " + minimum + "."));	
	}

}
