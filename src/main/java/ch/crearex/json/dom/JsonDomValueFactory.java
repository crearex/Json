package ch.crearex.json.dom;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;

public class JsonDomValueFactory implements JsonValueFactory {

	private final JsonContext context;
	
	public JsonDomValueFactory(JsonContext context) {
		this.context = context;
	}
	
	@Override
	public JsonSimpleValue createStringValue(String value) {
		return new JsonString(context, value);
	}

	@Override
	public JsonSimpleValue createNumberValue(String value) {
		return new JsonNumber(context, value);
	}

	@Override
	public JsonSimpleValue createBooleanValue(boolean value) {
		return new JsonBoolean(context, value);
	}

	@Override
	public JsonSimpleValue createNullValue() {
		return new JsonNull(context);
	}
	
}