package ch.crearex.json.impl;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;

public class JsonSimpleValueFactory implements JsonValueFactory {
	
	private final JsonContext context;
	
	// constructor package protected machen
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
