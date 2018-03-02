package ch.crearex.json.schema.constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class MinLengthConstraint implements SimpleValueConstraint {

	private final int minLength;
	
	public MinLengthConstraint(int minLength) {
		if(minLength < 0) {
			throw new JsonSchemaException("minLength must be >= 0.");
		}
		this.minLength = minLength;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(value.toString().length() >= minLength) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " '"+value.toString()+"' must be >= " + minLength + " characters.");
	}

}
