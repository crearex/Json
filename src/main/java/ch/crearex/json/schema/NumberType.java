package ch.crearex.json.schema;

import java.util.LinkedList;

import javax.swing.SpringLayout.Constraints;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonNumberValue;

public class NumberType extends ValueType {
	
	protected NumberType(String title, String description) {
		super(title, description);
	}
	
	@Override
	public String getTypeName() {
		return SchemaConstants.NUMBER_TYPE;
	}

	@Override
	public boolean matchesDomType(Class<?> type)  {
		return JsonNumberValue.class.isAssignableFrom(type);
	}
	
	@Override
	public String toString() {
		String retVal = SchemaConstants.NUMBER_TYPE;
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
