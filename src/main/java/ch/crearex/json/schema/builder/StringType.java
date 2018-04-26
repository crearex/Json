package ch.crearex.json.schema.builder;

import ch.crearex.json.schema.SchemaConstants;

public class StringType extends ValueType {

	public StringType(String title, String description) {
		super(title, description);
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.STRING_TYPE;
	}
	
	@Override
	public boolean matchesDomType(String typeName)  {
		return SchemaConstants.STRING_TYPE.equals(typeName);
	}
	
	@Override
	public String toString() {
		String retVal = SchemaConstants.STRING_TYPE;
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
