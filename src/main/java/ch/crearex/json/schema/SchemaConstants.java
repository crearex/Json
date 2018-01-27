package ch.crearex.json.schema;

public interface SchemaConstants {
	String SCHEMA_URI_NAME = "$schema";
	String SCHEMA_ID = "$id";
	String INTERNAL_REFERENCE = "$ref";
	String TYPE_NAME = "type";
	String DEFINITIONS = "definitions";
	
	String TITLE_NAME = "title";
	String DESCRIPTION_NAME = "description";
	String PROPERTIES_NAME = "properties";
	String ITEMS_NAME = "items";
	
	String OBJECT_TYPE = "object";
	String ARRAY_TYPE = "array";
	String STRING_TYPE = "string";
	String NUMBER_TYPE = "number";
	String BOOLEAN_TYPE = "boolean";
	String NULL_TYPE = "null";
	String ANY_TYPE = "any";
	
	String REQUIRED_NAME = "required";
	char HASH = '#';
	char PATH_SEPARATOR = '/';
	String INTERNAL_ID_PREFIX = "" + HASH + PATH_SEPARATOR;
	
	
	
}
