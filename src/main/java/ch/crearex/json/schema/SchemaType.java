package ch.crearex.json.schema;

public interface SchemaType {

	SchemaType ANY = new AnyType();
	boolean DEFAULT_NULLABLE = false;

	boolean matchesDomType(Class<?> type);
	boolean isNullable();
}
