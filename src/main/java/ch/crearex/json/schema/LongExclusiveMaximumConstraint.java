package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public class LongExclusiveMaximumConstraint implements Constraint {

	private final long maximum;
	
	public LongExclusiveMaximumConstraint(long maximum) {
		this.maximum = maximum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() < maximum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " >= " + maximum + "."));
	}

}