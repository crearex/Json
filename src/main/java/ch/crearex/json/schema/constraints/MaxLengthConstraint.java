package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class MaxLengthConstraint implements SimpleValueConstraint {

	private final int maxLength;
	
	public MaxLengthConstraint(int maxLength) {
		if(maxLength < 0) {
			throw new JsonSchemaException("maxLength must be >= 0.");
		}
		this.maxLength = maxLength;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.toString().length() <= maxLength) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " '"+value.toString()+"' must be <= " + maxLength + " characters.");
	}

}
