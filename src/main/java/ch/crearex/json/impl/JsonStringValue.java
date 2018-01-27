package ch.crearex.json.impl;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.SchemaConstants;

public class JsonStringValue extends JsonValImpl implements JsonSimpleValue {

	protected JsonStringValue(JsonContext context, String value) {
		super(context, value);
	}
	
	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.STRING_TYPE;
	}
	
}
