package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.SchemaConstants;

public class JsonBooleanValue extends JsonValImpl implements JsonSimpleValue {

	private final boolean booleanValue;
	
	protected JsonBooleanValue(JsonContext context, boolean booleanValue) {
		super(context, Boolean.toString(booleanValue));
		this.booleanValue = booleanValue;
	}
	
	@Override
	public Boolean asBoolean() {
		return booleanValue;
	}
	
	@Override
	public boolean isBoolean() {
		return true;
	}
	
	@Override
	public String toString() {
		if(booleanValue) {
			return JsonParser.JSON_TRUE;
		} else {
			return JsonParser.JSON_FALSE;
		}
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.BOOLEAN_TYPE;
	}

}
