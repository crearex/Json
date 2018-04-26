package ch.crearex.json.schema.builder;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.ContainerVisitor;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.ValueValidator;

public class ArrayType extends ContainerType {

	public static final ArrayType EMTPTY_ARRAY = new ArrayType("", "") {
		@SuppressWarnings("unused")
		void validateEntryValue(JsonSchemaContext context, JsonSimpleValue value) {
		}
	};
	static final SchemaList EMPTY_TYPE_LIST = new SchemaList(new SchemaType[0]);

	private SchemaList possibleItemTypes = null;
	private boolean uniqueItems = false;

	public ArrayType(String title, String description) {
		super(title, description);
	}

	@Override
	public String getTypeName() {
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
	public boolean matchesDomType(String typeName) {
		return SchemaConstants.ARRAY_TYPE.equals(typeName);
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
		for (SchemaType itemType : possibleItemTypes.getSchemaTypes()) {
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
	public SchemaType getEntryType(JsonSchemaContext context, int nextArrayIndex, String entryTypeName) {
		if ((possibleItemTypes == null) || (possibleItemTypes.size() == 0)) {
			return null;
		}

		if (possibleItemTypes.size() == 1) {
			SchemaType type = possibleItemTypes.getFirst();
			if (type.matchesDomType(entryTypeName)) {
				return type;
			}
		} else {
			if (possibleItemTypes.size() > nextArrayIndex) {
				SchemaType type = possibleItemTypes.get(nextArrayIndex);
				if (type.matchesDomType(entryTypeName)) {
					return type;
				}
			}
		}

		// create error message
		String expectedTypeName = possibleItemTypes.getFirst().getTypeName();
		if (possibleItemTypes.size() > 1) {
			if (possibleItemTypes.size() < nextArrayIndex) {
				expectedTypeName = possibleItemTypes.get(nextArrayIndex).getTypeName();
			} else {
				expectedTypeName = "No type for index " + nextArrayIndex + " defined";
			}
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(),
				"Invalid type in '" + context.getPath() + "'! Expected: [" + expectedTypeName + "]."));
		
		return null;
	}

	public void validateEntryValue(JsonSchemaContext context, int nextArrayIndex, JsonSimpleValue value) {
		if (possibleItemTypes == null) {
			return;
		}

		if (possibleItemTypes.size() == 1) {
			SchemaType type = possibleItemTypes.getFirst();
			if (type.isNullable() && value.isNull()) {
				return;
			}
			if (type.matchesDomType(value.getTypeName())) {
				if (type instanceof ValueType) {
					((ValueValidator) type).validate(context, value);
					return;
				}
			}
		} else if (possibleItemTypes.size() > nextArrayIndex) {
			SchemaType type = possibleItemTypes.get(nextArrayIndex);
			if (type.isNullable() && value.isNull()) {
				return;
			}
			if (type.matchesDomType(value.getTypeName())) {
				if (type instanceof ValueType) {
					((ValueValidator) type).validate(context, value);
					return;
				}
			}
		}

		// Type errors are reported by the type check in getEntryType()
	}

	public void setUniqueItems(boolean uniqueItems) {
		this.uniqueItems = uniqueItems;
	}

	public boolean isUniqueItems() {
		return uniqueItems;
	}
}
