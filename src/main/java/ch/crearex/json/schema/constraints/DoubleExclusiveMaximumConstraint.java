package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SimpleValueConstraint;

public class DoubleExclusiveMaximumConstraint implements SimpleValueConstraint {
	
	private final double maximum;
	
	public DoubleExclusiveMaximumConstraint(double maximum) {
		this.maximum = maximum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() < maximum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " >= " + maximum + "."));	
	}

}