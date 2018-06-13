package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

/**
 * JSON-Path beginning for a relative path - matches the current Object (not the {@link RootPathEntry})
 * @author Markus Niedermann
 *
 */
public class CurrentPathEntry extends JsonPathEntry {
	public CurrentPathEntry() {
		super(""+JsonPathParser.RELATIVE_OBJECT);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CurrentPathEntry;
	}
	
	@Override
	public int hashCode() {
		return (""+JsonPathParser.RELATIVE_OBJECT).hashCode();
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
