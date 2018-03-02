package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.ObjectValidationData;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;

public class MinPropertiesConstraint implements ContainerConstraint {

	private final int minProperties;
	
	public MinPropertiesConstraint(int minProperties) {
		this.minProperties = minProperties;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		if (!(validationData instanceof ObjectValidationData)) {
			return ValidationResult.OK;
		}
		ObjectValidationData objValidationData = (ObjectValidationData) validationData;
		if (objValidationData.getPropertyCount() >= minProperties) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(),
				"Validation failed! " + context.getPath() + " property count " + objValidationData.getPropertyCount()
						+ " must be >= " + minProperties + ".");
	}

}
