package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;

public class ArrayType extends ContainerType {
	
	private SchemaType[] possibleItemTypes = new SchemaType[] { SchemaType.ANY };
	
	public ArrayType(String title, String description) {
		super(title, description);
	}
	
	@Override
	public String getName() {
		return SchemaConstants.ARRAY_TYPE;
	}

	public void addItemTypes(SchemaType[] itemTypes) {
		this.possibleItemTypes = itemTypes;
	}
	
	public SchemaType[] getItemTypes() {
		return possibleItemTypes;
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return type == JsonArray.class;
	}

	@Override
	public String toString() {
		String retVal = SchemaConstants.ARRAY_TYPE;
		String title = getTitle();
		String description = getDescription();
		if(!title.isEmpty() || !description.isEmpty()) {
			retVal += ":";
		}
		if(!title.isEmpty()) {
			retVal += " " + title + ".";
		}
		if(!description.isEmpty()) {
			retVal += " " + description + ".";
		}
		return retVal;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
		visitor.visit(this);
		for(SchemaType itemType: this.possibleItemTypes) {
			if(itemType instanceof ContainerType) {
				((ContainerType)itemType).visit(visitor);
			}
		}
		
	}

	SchemaType getEntryType(JsonSchemaContext context, Class<?> entryType) {
		for(SchemaType type: possibleItemTypes) {
			if(type.matchesDomType(entryType)) {
				return type;
			}	
		}
		context.notifySchemaViolation("Unexpected type in '" + context.getPath() + "'! Expected: " + SchemaUtil.toStringSummary(possibleItemTypes));
		return SchemaType.ANY;
	}

	void validateEntryValue(JsonSchemaContext context, JsonSimpleValue value) {
		// TODO Auto-generated method stub
		
	}
}
