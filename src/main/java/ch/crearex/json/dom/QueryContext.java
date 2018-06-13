package ch.crearex.json.dom;

import java.util.LinkedList;
import java.util.List;

import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonPathEntry;

public class QueryContext {
	private LinkedList<JsonElement> result = new LinkedList<JsonElement>();
	private final JsonPath path;
	
	private int level = 1;
	
	public QueryContext(JsonPath path) {
		this.path = path;
	}
	
	public List<JsonElement> getResult() {
		return result;
	}
	
	void incLevel() {
		level++;
	}
	
	void decLevel() {
		level--;
	}
	
	boolean isLeafLevel() {
		return level == path.size() - 1;
	}
	
	JsonPathEntry getJsonPathEntry() {
		return path.get(level);
	}

	public void addResultEntry(JsonElement value) {
		result.add(value);
	}
}
