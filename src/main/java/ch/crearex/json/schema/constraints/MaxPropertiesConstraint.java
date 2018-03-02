package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.ObjectValidationData;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;

public class MaxPropertiesConstraint implements ContainerConstraint {

	private final int maxProperties;

	public MaxPropertiesConstraint(int maxProperties) {
		this.maxProperties = maxProperties;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		if (!(validationData instanceof ObjectValidationData)) {
			return ValidationResult.OK;
		}
		ObjectValidationData objValidationData = (ObjectValidationData) validationData;
		if (objValidationData.getPropertyCount() <= maxProperties) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(),
				"Validation failed! " + context.getPath() + " property count " + objValidationData.getPropertyCount()
						+ " must be <= " + maxProperties + ".");
	}

}
