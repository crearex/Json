package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SimpleValueConstraint;

public class DoubleExclusiveMinimumConstraint implements SimpleValueConstraint {

	private final double minimum;
	
	public DoubleExclusiveMinimumConstraint(double minimum) {
		this.minimum = minimum;
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asDouble() > minimum) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " <= " + minimum + "."));	
	}

}