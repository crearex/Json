package ch.crearex.json.schema;

public interface SchemaType {

	AnyType ANY = new AnyType();
	AnyType ANY_NULLABLE = new AnyType().setNullable(true);
	boolean DEFAULT_NULLABLE = false;
	
	boolean matchesDomType(Class<?> type);
	boolean isNullable();
	SchemaType setNullable(boolean nullable);
	String getTypeName();
}
