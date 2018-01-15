package ch.crearex.json;

/**
 * Simple {@link JsonCallback}-Container for two {@link JsonCallback JsonCallback}s.
 * @author Markus Niedermann
 *
 */
public class JsonYCallback implements JsonCallback {
	
	private final JsonCallback first;
	private final JsonCallback second;
	
	public JsonYCallback(JsonCallback first, JsonCallback second) {
		this.first = first;
		this.second = second;
	}
	
	public JsonCallback getFirst() {
		return first;
	}
	
	public JsonCallback getSecond() {
		return second;
	}

	@Override
	public void beginDocument(JsonContext context) {
		first.beginDocument(context);
		second.beginDocument(context);
	}

	@Override
	public void endDocument(JsonContext context) {
		first.endDocument(context);
		second.endDocument(context);
	}

	@Override
	public void beginObject(JsonContext context) {
		first.beginObject(context);
		second.beginObject(context);
	}

	@Override
	public void endObject(JsonContext context) {
		first.endObject(context);
		second.endObject(context);
	}

	@Override
	public void beginArray(JsonContext context) {
		first.beginArray(context);
		second.beginArray(context);
	}

	@Override
	public void endArray(JsonContext context) {
		first.endArray(context);
		second.endArray(context);
	}

	@Override
	public void property(JsonContext context, String propertyName) {
		first.property(context, propertyName);
		second.property(context, propertyName);
	}

	@Override
	public void simpleValue(JsonContext context, JsonSimpleValue value) {
		first.simpleValue(context, value);
		second.simpleValue(context, value);
	}

	@Override
	public void comma(JsonContext context) {
		first.comma(context);
		second.comma(context);
	}

}
