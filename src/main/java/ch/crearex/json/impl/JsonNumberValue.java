package ch.crearex.json.impl;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;

public class JsonNumberValue extends JsonValImpl implements JsonSimpleValue {

	protected JsonNumberValue(JsonContext context, String value) {
		super(context, value);
	}
	
	@Override
	public boolean isNumber() {
		return true;
	}
}
