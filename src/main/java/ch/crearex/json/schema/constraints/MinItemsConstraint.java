package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ArrayValidationData;
import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;

public class MinItemsConstraint implements ContainerConstraint {
	private final int minItems;
	
	public MinItemsConstraint(int minItems) {
		if(minItems < 0) {
			throw new JsonSchemaException("minItems must be >= 0.");
		}
		this.minItems = minItems;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		if(!(validationData instanceof ArrayValidationData)) {
			return ValidationResult.OK;
		}
		if(((ArrayValidationData)validationData).getNextArrayIndex() >= minItems) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " expected array size >= " + minItems + " items.");
	}
}