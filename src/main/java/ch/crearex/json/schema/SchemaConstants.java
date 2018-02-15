package ch.crearex.json.schema;

public interface SchemaConstants {
	String SCHEMA_URI_NAME = "$schema";
	String SCHEMA_ID = "$id";
	String INTERNAL_REFERENCE = "$ref";
	String TYPE_NAME = "type";
	String ENUM_NAME = "enum";
	String CONST_NAME = "const";
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
	
	String REQUIRED_PROPERTIES_CONSTRAINT = "required";
	char HASH = '#';
	char PATH_SEPARATOR = '/';
	String INTERNAL_ID_PREFIX = "" + HASH + PATH_SEPARATOR;
	
	String MAXIMUM_CONSTRAINT = "maximum";
	String MINIMUM_CONSTRAINT = "minimum";
	String EXCLUSIVE_MAXIMUM_CONSTRAINT = "exclusiveMaximum";
	String EXCLUSIVE_MINIMUM_CONSTRAINT = "exclusiveMinimum";
	String MULTIPLE_OF_CONSTRAINT = "multipleOf";
	String REGEX_CONSTRAINT = "regex";
	String PATTERN_CONSTRAINT = "pattern";
	String MAX_LENGTH_CONSTRAINT = "maxLength";
	String MIN_LENGTH_CONSTRAINT = "minLength";
	String MAX_ITEMS_CONSTRAINT = "maxItems";
	String MIN_ITEMS_CONSTRAINT = "minItems";
	String UNIQUE_ITEMS_CONSTRAINT = "uniqueItems";
	String MIN_PROPERTIES_CONSTRAINT = "minProperties";
	String MAX_PROPERTIES_CONSTRAINT = "maxProperties";
	
}
