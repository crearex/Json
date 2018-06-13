package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonStringValue;

public class JsonString extends JsonStringValue implements JsonSimpleValue, JsonElement {

	public JsonString(JsonContext context, String value) {
		super(context, value);
	}
	
	@Override
	public JsonString clone() {
		JsonString clone = new JsonString(getContext(), getRawValue());
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
