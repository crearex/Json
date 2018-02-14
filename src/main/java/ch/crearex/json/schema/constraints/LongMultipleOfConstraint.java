package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SimpleValueConstraint;

public class LongMultipleOfConstraint implements SimpleValueConstraint {
	
	private final long factor;
	
	public LongMultipleOfConstraint(long factor) {
		this.factor = factor;
		if(factor <= 0) {
			throw new JsonSchemaException(SchemaConstants.MULTIPLE_OF_CONSTRAINT + " constaint must be > 0!");
		}
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.asLong() % factor == 0) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " is not a multiple of " + factor + "."));	
	}

}
