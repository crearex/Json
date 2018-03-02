package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonBooleanValue;

public class BooleanType extends ValueType {

	protected BooleanType(String title, String description) {
		super(title, description);
	}
	
	@Override
	public String getTypeName() {
		return SchemaConstants.BOOLEAN_TYPE;
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return JsonBooleanValue.class.isAssignableFrom(type);
	}
	
	@Override
	public String toString() {
		String retVal = SchemaConstants.BOOLEAN_TYPE;
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
