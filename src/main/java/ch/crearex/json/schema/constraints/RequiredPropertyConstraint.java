package ch.crearex.json.schema.constraints;

import java.util.HashSet;
import java.util.Set;

import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.ObjectType;
import ch.crearex.json.schema.ObjectValidationData;
import ch.crearex.json.schema.ValidationData;

public class RequiredPropertyConstraint implements ContainerConstraint {

	private final Set<String> requiredPropertyNames;
	
	public RequiredPropertyConstraint(Set<String> requiredPropertyNames) {
		this.requiredPropertyNames = requiredPropertyNames;
	}

	@Override
	public void validate(JsonSchemaContext context, ValidationData validationData) {
		if(!(validationData instanceof ObjectValidationData)) {
			return;
		}
		ObjectValidationData objectValidationData = (ObjectValidationData)validationData;
		HashSet<String> readPropertyNames = objectValidationData.getReadPropertyNames();
		for(String requiredProperty: requiredPropertyNames) {
			if(!readPropertyNames.contains(requiredProperty)) {
				context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Required Property '" + context.getPath().concat(requiredProperty) + "' missing!"));
			}
		}
	}

}
