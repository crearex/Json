package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonArray;

public class ArrayType extends ContainerType {
	
	private SchemaType[] itemType = new SchemaType[] { SchemaType.ANY };
	
	public ArrayType(String title, String description) {
		super(title, description);
	}

	public void addItemType(SchemaType[] itemType) {
		this.itemType = itemType;
	}
	
	public SchemaType[] getItemType() {
		return itemType;
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return type == JsonArray.class;
	}

	@Override
	public String toString() {
		String retVal = "Array";
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
}
