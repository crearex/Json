package ch.crearex.json.schema;

public interface ContainerConstraint {
	ValidationResult validate(JsonSchemaContext context, ValidationData validationData);
}
