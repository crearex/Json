package ch.crearex.json.schema.constraints;

import ch.crearex.json.schema.ArrayValidationData;
import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.ValidationData;

public class MaxItemsConstraint implements ContainerConstraint {
	private final int maxItems;
	
	public MaxItemsConstraint(int maxItems) {
		if(maxItems < 0) {
			throw new JsonSchemaException("maxItems must be >= 0.");
		}
		this.maxItems = maxItems;
	}

	@Override
	public void validate(JsonSchemaContext context, ValidationData validationData) {
		if(!(validationData instanceof ArrayValidationData)) {
			return;
		}
		if(((ArrayValidationData)validationData).getNextArrayIndex() <= maxItems) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! " + context.getPath() + " expected array size <= " + maxItems + " items."));
	}
}
