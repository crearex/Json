package ch.crearex.json.dom;

import ch.crearex.json.IndexToken;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPathException;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.PropertyToken;
import ch.crearex.json.RelativeToken;
import ch.crearex.json.RootToken;
import ch.crearex.json.Token;

/**
 * Common root class for a {@link JsonObject} and a {@link JsonArray}
 * @author Markus Niedermann
 *
 */
public abstract class JsonContainer implements JsonElement {
	
	private JsonContainer parent;
	
	protected JsonContainer(JsonContainer parent) {
		this.parent = parent;
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public JsonContainer getParent() {
		return parent;
	}
	
	void setParent(JsonContainer parent) {
		this.parent = parent;
	}
	
	public JsonPath getPath() {
		JsonPath path = new JsonPath();
		JsonContainer child = this;
		JsonContainer myParent = child.parent;
		while(myParent != null) {
			path.addFirst(myParent.getPathEntryForChild(child));
			child = myParent;
			myParent = child.parent;
		}
		path.addFirst(new RootToken());
		return path;
	}
	
	public JsonSimpleValue getValue(JsonPath path) {
		JsonContainer container = this;
		if(path.getFirst() instanceof RootToken) {
			while(container.parent != null) {
				container = container.parent;
			}
		}
		for(Token entry: path) {
			if(path.isLastEntry(entry)) {
				if((entry instanceof PropertyToken) && (container instanceof JsonObject)) {
					return ((JsonObject)container).getValue(entry.getName());
				} else if((entry instanceof IndexToken) && (container instanceof JsonArray)) {
					return ((JsonArray)container).getValue(((IndexToken)entry).getIndex());
				} else {
					throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
				}
			}
			
			if((entry instanceof PropertyToken) && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getName());
			} else if((entry instanceof IndexToken) && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(((IndexToken)entry).getIndex());
			} else if((entry instanceof RootToken) || (entry instanceof RelativeToken)) {
				// do nothing
			} else {
				throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
			}
		}
		return null;
	}
	
	public JsonObject getParentObject() {
		if(parent instanceof JsonObject) {
			return (JsonObject)parent;
		}
		return null;
	}
	
	public JsonArray getParentArray() {
		if(parent instanceof JsonArray) {
			return (JsonArray)parent;
		}
		return null;
	}
	
	/**
	 * Finds the root of the JSON Document and adds the <b>value</b>
	 * at the position defined by <b>path</b>. The path (JSON structure) 
	 * to the insert position must already exist!
	 */
	public JsonContainer add(JsonPath path, JsonElement value) {
		JsonContainer container = this;
		if(path.getFirst() instanceof RootToken) {
			while(this.parent != null) {
				container = this.parent;
			}
		}
		
		for(Token entry: path) {
			if(path.isLastEntry(entry)) {
				break;
			}

			if((entry instanceof PropertyToken) && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getName());
			} else if((entry instanceof IndexToken) && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(((IndexToken)entry).getIndex());
			} else if((entry instanceof RootToken) || (entry instanceof RelativeToken)) {
				// to nothing
			} else {
				throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
			}
		}
		
		if(container instanceof JsonObject) {
			((JsonObject)container).add(path.getLast().getName(), value);
		} else if(container instanceof JsonArray) {
			((JsonArray)container).add(((IndexToken)path.getLast()).getIndex(), value);
		} else {
			throw new JsonAccessException("Path " + path + " does not exist!");
		}
		
		return this;
	}

	public String prettyPrint() {
		StringBuffer buffer = new StringBuffer();
		JsonPrettyFormatter formatter = new JsonPrettyFormatter(buffer);
		JsonDocument doc = JsonDocument.createEmptyDocument().setRoot(this);
		doc.traverse(formatter);
		return buffer.toString();
	}

	public JsonContainer getContainer(int index) {
		return null;
	}
	
	public abstract JsonContainer getContainer(String propertyName);
	
	public abstract JsonContainer clear();
	
	protected abstract Token getPathEntryForChild(JsonElement child);
	
	protected abstract String resolvePathStringEntry(JsonElement value);
}
