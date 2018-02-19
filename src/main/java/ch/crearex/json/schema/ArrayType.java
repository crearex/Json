package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;

public class ArrayType extends ContainerType {

	static final ArrayType EMTPTY_ARRAY = new ArrayType("", "") {
		@SuppressWarnings("unused")
		void validateEntryValue(JsonSchemaContext context, JsonSimpleValue value) {
		}
	};
	private static final SchemaList EMPTY_TYPE_LIST = new SchemaList(new SchemaType[0]);

	private SchemaList possibleItemTypes = null;
	private boolean uniqueItems = false;

	public ArrayType(String title, String description) {
		super(title, description);
	}

	@Override
	public String getName() {
		return SchemaConstants.ARRAY_TYPE;
	}

	public void addItemTypes(SchemaList itemTypes) {
		this.possibleItemTypes = itemTypes;
	}

	public SchemaList getItemTypes() {
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
		for (SchemaType itemType : possibleItemTypes.getSchemata()) {
			if (itemType instanceof ContainerType) {
				((ContainerType) itemType).visit(visitor);
			}
		}
	}

	/**
	 * Returns the type of the array entry or null if not found!
	 * 
	 * @param nextArrayIndex
	 */
	SchemaType getEntryType(JsonSchemaContext context, int nextArrayIndex, Class<?> entryType) {
		if ((possibleItemTypes == null) || (possibleItemTypes.size() == 0)) {
			return null;
		}

		if (possibleItemTypes.size() == 1) {
			SchemaType type = possibleItemTypes.getFirst();
			if (type.matchesDomType(entryType)) {
				return type;
			}
		} else {
			if (possibleItemTypes.size() > nextArrayIndex) {
				if (possibleItemTypes.size() >= nextArrayIndex) {
					SchemaType type = possibleItemTypes.get(nextArrayIndex);
					if (type.matchesDomType(entryType)) {
						return type;
					}
				}
			}
		}

		String expectedTypeName = possibleItemTypes.getFirst().getName();
		if (possibleItemTypes.size() > 1) {
			if (possibleItemTypes.size() < nextArrayIndex) {
				expectedTypeName = possibleItemTypes.get(nextArrayIndex).getName();
			} else {
				expectedTypeName = "No type for index " + nextArrayIndex + " defined";
			}
		}

		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(),
				"Invalid type in '" + context.getPath() + "'! Expected: [" + expectedTypeName + "]."));
		return null;
	}

	void validateEntryValue(JsonSchemaContext context, int nextArrayIndex, JsonSimpleValue value) {
		if (possibleItemTypes == null) {
			return;
		}

		int index = 0;
		if (possibleItemTypes.size() == 1) {
			SchemaType type = possibleItemTypes.getFirst();
			if (type.isNullable() && value.isNull()) {
				return;
			}
			if (type.matchesDomType(value.getClass())) {
				if (type instanceof ValueType) {
					((ValueType) type).validate(context, "" + index, value);
					return;
				}
			}
		} else if (possibleItemTypes.size() > nextArrayIndex) {
			SchemaType type = possibleItemTypes.get(nextArrayIndex);
			if (type.isNullable() && value.isNull()) {
				return;
			}
			if (type.matchesDomType(value.getClass())) {
				if (type instanceof ValueType) {
					((ValueType) type).validate(context, "" + index, value);
					return;
				}
			}
		}

		// Type errors are reported by the type check in getEntryType()
	}

	void setUniqueItems(boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
	}

	public boolean isUniqueItems() {
		return uniqueItems;
	}
}
