package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonNumberValue;

public class JsonNumber extends JsonNumberValue implements JsonSimpleValue, JsonElement {

	protected JsonNumber(JsonContext context, String value) {
		super(context, value);
	}

	@Override
	public void traverse(JsonDomContext context, JsonCallback visitor) {
		context.notifyValue(this);
	}
	
	@Override
	public JsonPath getPath() {
		return getContext().getPath();
	}

}
