package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

public abstract class JsonPathEntry {
	private StringBuilder name = new StringBuilder();
	JsonPathEntry() {
	}
	JsonPathEntry(String name) {
		this.name.append(name);
	}
	void append(char ch) {
		this.name.append(ch);
	}
	@Override
	public String toString() {
		return name.toString();
	}
	public String getName() {
		return name.toString();
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean selectProperty(String propertyName, JsonElement value);
	public abstract boolean selectArrayEntry(int index, JsonElement value);
}