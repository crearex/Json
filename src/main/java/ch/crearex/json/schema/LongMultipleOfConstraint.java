package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public class LongMultipleOfConstraint implements Constraint {
	
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
