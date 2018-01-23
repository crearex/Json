package ch.crearex.json.schema;

import ch.crearex.json.impl.JsonStringValue;

public class StringType extends ValueType {

	protected StringType(String title, String description) {
		super(title, description);
	}

	@Override
	public String getName() {
		return SchemaConstants.STRING_TYPE;
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return JsonStringValue.class.isAssignableFrom(type);
	}
	
	@Override
	public String toString() {
		String retVal = "String";
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
