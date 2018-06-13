package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;

import java.util.Objects;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.schema.SchemaConstants;

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

	@Override
	public String getTypeName() {
		return SchemaConstants.NULL_TYPE;
	}

}
