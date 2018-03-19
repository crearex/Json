package ch.crearex.json.dom;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.impl.JsonBooleanValue;
import ch.crearex.json.impl.JsonNullValue;
import ch.crearex.json.impl.JsonNumberValue;
import ch.crearex.json.impl.JsonStringValue;

/**
 * The value factory which creates the {@link JsonSimpleValue}s for a JSON DOM Tree.
 * 
 * @author Markus Niedermann
 *
 */
public class JsonDomValueFactory implements JsonValueFactory {

	private final JsonDomContext context;
	
	public JsonDomValueFactory(JsonDomContext context) {
		this.context = context;
	}
	
	protected JsonDomContext getContext() {
		return context;
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
