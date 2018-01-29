package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.impl.JsonNullValue;

public class ArrayType extends ContainerType {

	static final ArrayType EMTPTY_ARRAY = new ArrayType("", "") {
		void validateEntryValue(JsonSchemaContext context, JsonSimpleValue value) {
		}
	};
	private static final SchemaType[] EMPTY_TYPE_LIST = new SchemaType[0];

	private SchemaType[] possibleItemTypes = null;

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
		if (possibleItemTypes == null) {
			return EMPTY_TYPE_LIST;
		}
		return possibleItemTypes;
	}

	@Override
	public boolean matchesDomType(Class<?> type) {
		return type == JsonArray.class;
	}

	@Override
	public String toString() {
		String retVal = SchemaConstants.ARRAY_TYPE;
		String title = getTitle();
		String description = getDescription();
		if (!title.isEmpty() || !description.isEmpty()) {
			retVal += ":";
		}
		if (!title.isEmpty()) {
			retVal += " " + title + ".";
		}
		if (!description.isEmpty()) {
			retVal += " " + description + ".";
		}
		return retVal;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
		visitor.visit(this);

		if (possibleItemTypes == null) {
			return;
		}
		for (SchemaType itemType : possibleItemTypes) {
			if (itemType instanceof ContainerType) {
				((ContainerType) itemType).visit(visitor);
			}
		}
	}

	/**
	 * Returns the type of the array entry or null if not found!
	 */
	SchemaType getEntryType(JsonSchemaContext context, Class<?> entryType) {
		if((possibleItemTypes == null) || (possibleItemTypes.length == 0)) {
			return null;
		}
		for(SchemaType type: possibleItemTypes) {
			if(type.matchesDomType(entryType)) {
				return type;
			}	
		}
		
		if(JsonNullValue.class.isAssignableFrom(entryType)) {
			if(possibleItemTypes[0].isNullable()) {
				return possibleItemTypes[0];
			}
		}
		
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected type in '" + 
				context.getPath() + "'! Expected: " + SchemaUtil.toStringSummary(possibleItemTypes)));
		return null;
	}

	void validateEntryValue(JsonSchemaContext context, JsonSimpleValue value) {
		// TODO Auto-generated method stub

	}
}
