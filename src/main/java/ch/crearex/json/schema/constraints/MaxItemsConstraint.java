package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ArrayValidationData;
import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;

public class MaxItemsConstraint implements ContainerConstraint {
	private final int maxItems;
	
	public MaxItemsConstraint(int maxItems) {
		if(maxItems < 0) {
			throw new JsonSchemaException("maxItems must be >= 0.");
		}
		this.maxItems = maxItems;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		if(!(validationData instanceof ArrayValidationData)) {
			return ValidationResult.OK;
		}
		if(((ArrayValidationData)validationData).getNextArrayIndex() <= maxItems) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! " + context.getPath() + " expected array size <= " + maxItems + " items.");
	}
}
