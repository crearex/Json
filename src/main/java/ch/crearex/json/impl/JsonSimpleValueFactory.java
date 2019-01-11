package ch.crearex.json.impl;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.dom.JsonBoolean;
import ch.crearex.json.dom.JsonNull;
import ch.crearex.json.dom.JsonNumber;
import ch.crearex.json.dom.JsonString;

public class JsonSimpleValueFactory implements JsonValueFactory {
	
	private final JsonContext context;
	
	public JsonSimpleValueFactory(JsonContext context) {
		this.context = context;
	}

	@Override
	public JsonSimpleValue createStringValue(String value) {
		return new JsonStringValue(context, value);
	}

	@Override
	public JsonSimpleValue createNumberValue(String value) {
		return new JsonNumberValue(context, value);
	}

	@Override
	public JsonSimpleValue createBooleanValue(boolean value) {
		return new JsonBooleanValue(context, value);
	}

	@Override
	public JsonSimpleValue createNullValue() {
		return new JsonNullValue(context);
	}

}
