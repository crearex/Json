package ch.crearex.json.schema;

public interface SchemaType {

	SchemaType ANY = new AnyType();
	SchemaType ANY_NULLABLE = new AnyType().setNullable(true);
	boolean DEFAULT_NULLABLE = false;
	
	boolean matchesDomType(Class<?> type);
	boolean isNullable();
	String getName();
}
