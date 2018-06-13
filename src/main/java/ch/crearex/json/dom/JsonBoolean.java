package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonBooleanValue;

public class JsonBoolean extends JsonBooleanValue implements JsonSimpleValue, JsonElement {
	public JsonBoolean(JsonContext context, boolean value) {
		super(context, value);
	}
	
	@Override
	public JsonBoolean clone() {
		JsonBoolean clone = new JsonBoolean(getContext(), asBoolean());
		clone.setReplacedValue(getReplacedValue());
		return clone;
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
