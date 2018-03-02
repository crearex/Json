package ch.crearex.json.schema;

public interface ObjectValidator {

	ValidationResult validate(JsonSchemaContext context, ValidationData validationData);

}