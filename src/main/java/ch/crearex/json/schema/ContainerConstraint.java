package ch.crearex.json.schema;

public interface ContainerConstraint {
	void validate(JsonSchemaContext context, ValidationData validationData);
}
