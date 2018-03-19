package ch.crearex.json.dom;

import java.util.Stack;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.impl.JsonContextImpl;
import ch.crearex.json.impl.JsonSimpleValueFactory;
import ch.crearex.json.impl.Source;

public class JsonDomContext extends JsonContextImpl {

	private Stack<JsonElement> elementStack = new Stack<JsonElement>();	

	public JsonDomContext(JsonCallback callback) {
		super(callback);
	}
	
	@Override
	public int getLevel() {
		return elementStack.size();
	}

	
	@Override
	public void reset() {
		super.reset();
		elementStack.clear();
	}

	protected void pushJsonElement(JsonElement item) {
		elementStack.add(item);
	}
	
	protected JsonElement popJsonElement() {
		return elementStack.pop();
	}
	
	public JsonElement peekJsonElement() {
		if(elementStack.isEmpty()) {
			return null;
		}
		return elementStack.peek();
	}

	protected void notifyBeginDocument(JsonCallback callback) {
		callback.beginDocument(this);
	}

	protected void notifyEndDocument(JsonCallback callback) {
		callback.endDocument(this);
	}
	
	@Override
	public JsonDomContext setSchemaCallback(JsonSchemaCallback schemaCallback) {
		super.setSchemaCallback(schemaCallback);
		return this;
	}
	
}
