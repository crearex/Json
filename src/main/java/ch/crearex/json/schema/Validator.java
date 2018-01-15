package ch.crearex.json.schema;

public interface Validator {
	SchemaType validateArrayEntryType(JsonSchemaContext context, Class<?> type);
	SchemaType validatePropertyType(JsonSchemaContext context, String propertyName, Class<?> type);
}
