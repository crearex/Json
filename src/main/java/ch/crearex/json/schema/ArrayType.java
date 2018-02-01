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
	 * @param nextArrayIndex 
	 */
	SchemaType getEntryType(JsonSchemaContext context, int nextArrayIndex, Class<?> entryType) {
		if((possibleItemTypes == null) || (possibleItemTypes.length == 0)) {
			return null;
		}
		
		if(possibleItemTypes.length == 1) {
			SchemaType type = possibleItemTypes[0];
			if(type.matchesDomType(entryType)) {
				return type;
			}
		} else {
			if(possibleItemTypes.length > nextArrayIndex) {
				if(possibleItemTypes.length >= nextArrayIndex) {
					SchemaType type = possibleItemTypes[nextArrayIndex];
					if(type.matchesDomType(entryType)) {
						return type;
					}
				}
			}
		}
		
		
		String expectedTypeName = possibleItemTypes[0].getName();
		if(possibleItemTypes.length > 1) {
			if(possibleItemTypes.length < nextArrayIndex) {
				expectedTypeName = possibleItemTypes[nextArrayIndex].getName();
			} else {
				expectedTypeName = "No type for index " + nextArrayIndex + " defined";
			}
		}
		
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Invalid type in '" + 
				context.getPath() + "'! Expected: [" + expectedTypeName + "]."));
		return null;
	}

	void validateEntryValue(JsonSchemaContext context, int nextArrayIndex, JsonSimpleValue value) {
		if(possibleItemTypes == null) {
			return;
		}
		
		int index = 0;
		boolean typeFound = false;
		
		if(possibleItemTypes.length == 1) {
			SchemaType type = possibleItemTypes[0];
			if(type.isNullable() && value.isNull()) {
				return;
			}
			if(type.matchesDomType(value.getClass())) {
				typeFound = true;
				if(type instanceof ValueType) {
					((ValueType)type).validate(context, ""+index , value);
					return;
				}
			}
		} else if(possibleItemTypes.length > nextArrayIndex) {
			SchemaType type = possibleItemTypes[nextArrayIndex];
			if(type.isNullable() && value.isNull()) {
				return;
			}
			if(type.matchesDomType(value.getClass())) {
				typeFound = true;
				if(type instanceof ValueType) {
					((ValueType)type).validate(context, ""+index , value);
					return;
				}
			}
		}
		
// Type errors are reported by the type check in getEntryType()		
//		if(!typeFound) {
//			String expectedTypeName = possibleItemTypes[0].getName();
//			if(possibleItemTypes.length > 1) {
//				if(possibleItemTypes.length < nextArrayIndex) {
//					expectedTypeName = possibleItemTypes[nextArrayIndex].getName();
//				} else {
//					expectedTypeName = "No type for index " + nextArrayIndex + " defined";
//				}
//			}
//			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Illegal array type '"+value.getTypeName()+"' for '"+context.getPath()+"'! Expected: [" + getPossibleTypeList() + "]."));
//		}

	}

	private String getPossibleTypeList() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(SchemaType type: possibleItemTypes) {
			if(first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(type.getName());
		}
		return builder.toString();
	}
}
