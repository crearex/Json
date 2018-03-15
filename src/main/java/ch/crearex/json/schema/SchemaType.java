package ch.crearex.json.schema;

public interface SchemaType {
	boolean DEFAULT_NULLABLE = false;
	boolean isNullable();
	SchemaType setNullable(boolean nullable);
	boolean matchesDomType(Class<?> type);
	String getTypeName();
}
