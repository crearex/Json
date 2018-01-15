package ch.crearex.json.schema;

public class ArrayTypeValidator implements Validator {

	private final SchemaType[] itemType;
	
	ArrayTypeValidator(ArrayType type) {
		this.itemType = type.getItemType();
	}


	@Override
	public SchemaType validateArrayEntryType(JsonSchemaContext context, Class<?> entryType) {
		for(SchemaType type: itemType) {
			if(type.matchesDomType(entryType)) {
				return type;
			}	
		}
		
		context.notifySchemaViolation("Unexpected Entry Type in " + context.getPath() + "!");
		return SchemaType.ANY;
	}

	@Override
	public SchemaType validatePropertyType(JsonSchemaContext context, String propertyName, Class<?> entryType) {
		context.notifySchemaViolation("Unexpected Object Validation for " + context.getPath() + "!");
		return SchemaType.ANY;
	}

}
