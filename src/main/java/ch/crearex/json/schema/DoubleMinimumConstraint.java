package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public class DoubleMinimumConstraint implements Constraint {

	private final double minimum;
	
	public DoubleMinimumConstraint(double minimum) {
		this.minimum = minimum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() >= minimum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " < " + minimum + "."));	
	}

}
