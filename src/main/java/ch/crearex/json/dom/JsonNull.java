package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonBooleanValue;
import ch.crearex.json.impl.JsonNullValue;

public class JsonNull extends JsonNullValue implements JsonSimpleValue, JsonElement {

	protected JsonNull(JsonContext context) {
		super(context);
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