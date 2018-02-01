package ch.crearex.json.schema;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;

public class DoubleMaximumConstraint implements Constraint {
	private final double maximum;
	public DoubleMaximumConstraint(double maximum) {
		this.maximum = maximum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() <= maximum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " > " + maximum + "."));	
	}

}
