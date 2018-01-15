package ch.crearex.json.schema;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;

public class ObjectTypeValidator implements Validator {

	private final ObjectType type;
	
	ObjectTypeValidator(ObjectType type) {
		this.type = type;
	}
	@Override
	public SchemaType validateArrayEntryType(JsonSchemaContext context, Class<?> propertyType) {
		context.notifySchemaViolation("Unexpected Array Validation for " + context.getPath() + "!");
		return SchemaType.ANY;
	}
	
	@Override
	public SchemaType validatePropertyType(JsonSchemaContext context, String propertyName, Class<?> propertyType) {
		SchemaType[] schemaPropertyType = type.getPropertyType(propertyName);
		for(SchemaType type: schemaPropertyType) {
			if(type.matchesDomType(propertyType)) {
				return type;
			}	
		}
		
		context.notifySchemaViolation("Unexpected Property Type for " + context.getPath() + "!");
		return SchemaType.ANY;
	}

}
