package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;

public class JsonNullValue extends JsonValImpl implements JsonSimpleValue {
	
	protected JsonNullValue(JsonContext context) {
		super(context, null);
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
	
	@Override
	public String toString() {
		return JsonParser.JSON_NULL;
	}

}
