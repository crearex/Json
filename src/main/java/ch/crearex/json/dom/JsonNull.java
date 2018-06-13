package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.JsonNullValue;

public class JsonNull extends JsonNullValue implements JsonSimpleValue, JsonElement {

	public JsonNull(JsonContext context) {
		super(context);
	}
	
	@Override
	public JsonNull clone() {
		JsonNull clone = new JsonNull(getContext());
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