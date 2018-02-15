package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.ObjectValidationData;
import ch.crearex.json.schema.ValidationData;

public class MaxPropertiesConstraint implements ContainerConstraint {

	private final int maxProperties;

	public MaxPropertiesConstraint(int maxProperties) {
		this.maxProperties = maxProperties;
	}

	@Override
	public void validate(JsonSchemaContext context, ValidationData validationData) {
		if (!(validationData instanceof ObjectValidationData)) {
			return;
		}
		ObjectValidationData objValidationData = (ObjectValidationData) validationData;
		if (objValidationData.getPropertyCount() <= maxProperties) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(),
				"Validation failed! " + context.getPath() + " property count " + objValidationData.getPropertyCount()
						+ " must be <= " + maxProperties + "."));
	}

}
