package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ArrayValidationData;
import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.ValidationData;

public class MinItemsConstraint implements ContainerConstraint {
	private final int minItems;
	
	public MinItemsConstraint(int minItems) {
		if(minItems < 0) {
			throw new JsonSchemaException("minItems must be >= 0.");
		}
		this.minItems = minItems;
	}

	@Override
	public void validate(JsonSchemaContext context, ValidationData validationData) {
		if(!(validationData instanceof ArrayValidationData)) {
			return;
		}
		if(((ArrayValidationData)validationData).getNextArrayIndex() >= minItems) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " expected array size >= " + minItems + " items."));
	}
}