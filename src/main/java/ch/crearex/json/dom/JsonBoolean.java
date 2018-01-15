package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonBooleanValue;

public class JsonBoolean extends JsonBooleanValue implements JsonSimpleValue, JsonElement {
	protected JsonBoolean(JsonContext context, boolean value) {
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
