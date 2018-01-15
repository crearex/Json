package ch.crearex.json.dom;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPathEntry;
import ch.crearex.json.JsonPathException;
import ch.crearex.json.JsonPrettyFormatter;

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
		JsonContainer prev = this.parent;
		while(prev != null) {
			path.addFirst(prev.getPathEntryForChild(this));
			prev = prev.parent;
		}
		path.addFirst(JsonPathEntry.createEmptyEntry(true));
		return path;
	}
	
	public JsonSimpleValue getValue(JsonPath path) {
		JsonContainer container = this;
		if(path.getFirst().isRoot()) {
			while(container.parent != null) {
				container = container.parent;
			}
		}
		for(JsonPathEntry entry: path) {
			if(path.isLastEntry(entry)) {
				if(entry.isPropertyNameEntry() && (container instanceof JsonObject)) {
					return ((JsonObject)container).getValue(entry.getPropertyName());
				} else if(entry.isArrayIndexEntry() && (container instanceof JsonArray)) {
					return ((JsonArray)container).getValue(entry.getArrayIndex());
				} else {
					throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
				}
			}
			
			if(entry.isPropertyNameEntry() && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getPropertyName());
			} else if(entry.isArrayIndexEntry() && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(entry.getArrayIndex());
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
		if(path.getFirst().isRoot()) {
			while(this.parent != null) {
				container = this.parent;
			}
		}
		
		for(JsonPathEntry entry: path) {
			if(path.isLastEntry(entry)) {
				break;
			}

			if(entry.isPropertyNameEntry() && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getPropertyName());
			} else if(entry.isArrayIndexEntry() && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(entry.getArrayIndex());
			} else {
				throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
			}
		}
		
		if(container instanceof JsonObject) {
			((JsonObject)container).add(path.getLast().getPropertyName(), value);
		} else if(container instanceof JsonArray) {
			((JsonArray)container).add(path.getLast().getArrayIndex(), value);
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
	
	protected abstract JsonPathEntry getPathEntryForChild(JsonContainer child);
	
	protected abstract String resolvePathStringEntry(JsonElement value);
}
