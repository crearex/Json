package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

/**
 * Matches the JSON-Root Object.
 * @author Markus Niedermann
 *
 */
public class RootPathEntry extends JsonPathEntry {
	public RootPathEntry() {
		super(""+JsonPathParser.ROOT_OBJECT);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RootPathEntry;
	}
	
	@Override
	public int hashCode() {
		return (""+JsonPathParser.ROOT_OBJECT).hashCode();
	}

	@Override
	public boolean selectProperty(String propertyName, JsonElement value) {
		return false;
	}

	@Override
	public boolean selectArrayEntry(int index, JsonElement value) {
		return false;
	}
}
