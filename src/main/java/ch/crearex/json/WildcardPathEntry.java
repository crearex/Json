package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

public class WildcardPathEntry extends JsonPathEntry {

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof WildcardPathEntry)) {
            return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return "*".hashCode();
	}

	@Override
	public boolean selectProperty(String propertyName, JsonElement value) {
		return true;
	}

	@Override
	public boolean selectArrayEntry(int index, JsonElement value) {
		return true;
	}

}
