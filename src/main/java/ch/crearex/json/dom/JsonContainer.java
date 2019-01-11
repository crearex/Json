package ch.crearex.json.dom;

import ch.crearex.json.IndexPathEntry;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPathException;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.PropertyPathEntry;

import java.util.List;

import ch.crearex.json.CurrentPathEntry;
import ch.crearex.json.RootPathEntry;
import ch.crearex.json.JsonPathEntry;

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
		
	@Override
	public abstract JsonContainer clone();
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public abstract int size();
	
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
		path.addFirst(new RootPathEntry());
		return path;
	}
	
	public JsonSimpleValue getValue(JsonPath path) {
		JsonContainer container = this;
		if(path.getFirst() instanceof RootPathEntry) {
			while(container.parent != null) {
				container = container.parent;
			}
		}
		for(JsonPathEntry entry: path) {
			if(path.isLastEntry(entry)) {
				if((entry instanceof PropertyPathEntry) && (container instanceof JsonObject)) {
					return ((JsonObject)container).getValue(entry.getName());
				} else if((entry instanceof IndexPathEntry) && (container instanceof JsonArray)) {
					return ((JsonArray)container).getValue(((IndexPathEntry)entry).getIndex());
				} else {
					throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
				}
			}
			
			if((entry instanceof PropertyPathEntry) && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getName());
			} else if((entry instanceof IndexPathEntry) && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(((IndexPathEntry)entry).getIndex());
			} else if((entry instanceof RootPathEntry) || (entry instanceof CurrentPathEntry)) {
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
		if(path.getFirst() instanceof RootPathEntry) {
			while(this.parent != null) {
				container = this.parent;
			}
		}
		
		for(JsonPathEntry entry: path) {
			if(path.isLastEntry(entry)) {
				break;
			}

			if((entry instanceof PropertyPathEntry) && (container instanceof JsonObject)) {
				container = ((JsonObject)container).getContainer(entry.getName());
			} else if((entry instanceof IndexPathEntry) && (container instanceof JsonArray)) {
				container = ((JsonArray)container).getContainer(((IndexPathEntry)entry).getIndex());
			} else if((entry instanceof RootPathEntry) || (entry instanceof CurrentPathEntry)) {
				// to nothing
			} else {
				throw new JsonPathException("JSON path '"+path+"' mismatch: " + entry, path);
			}
		}
		
		if(container instanceof JsonObject) {
			((JsonObject)container).add(path.getLast().getName(), value);
		} else if(container instanceof JsonArray) {
			((JsonArray)container).add(((IndexPathEntry)path.getLast()).getIndex(), value);
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
	
	abstract void query(QueryContext context);
	
	abstract public List<JsonElement> query(JsonPath path);
	
	protected abstract JsonPathEntry getPathEntryForChild(JsonElement child);
	
	protected abstract String resolvePathStringEntry(JsonElement value);

	
	
}
