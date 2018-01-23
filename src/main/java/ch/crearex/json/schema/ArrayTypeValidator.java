package ch.crearex.json.schema;

public class ArrayTypeValidator implements Validator {

	private final SchemaType[] possibleTypes;
	
	ArrayTypeValidator(ArrayType type) {
		this.possibleTypes = type.getItemTypes();
	}


	@Override
	public SchemaType validateArrayEntryType(JsonSchemaContext context, Class<?> entryType) {
		for(SchemaType type: possibleTypes) {
			if(type.matchesDomType(entryType)) {
				return type;
			}	
		}
		
		context.notifySchemaViolation("Unexpected type in '" + context.getPath() + "'! Expected: " + SchemaUtil.toStringSummary(possibleTypes));
		return SchemaType.ANY;
	}

	@Override
	public SchemaType validatePropertyType(JsonSchemaContext context, String propertyName, Class<?> entryType) {
		context.notifySchemaViolation("Unexpected Object Validation for '" + context.getPath() + "'!");
		return SchemaType.ANY;
	}

}
