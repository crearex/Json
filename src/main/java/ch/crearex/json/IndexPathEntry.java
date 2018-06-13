package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

/**
 * Matches an entry in an array.
 * @author Markus Niedermann
 *
 */
public class IndexPathEntry extends JsonPathEntry {
	private final int index;
	public IndexPathEntry(int index) {
		super(""+index);
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IndexPathEntry)) {
            return false;
		}
		IndexPathEntry comp = (IndexPathEntry) obj;
		return this.index == comp.index;
	}
	
	@Override
	public int hashCode() {
		return 31 * index;
	}
	@Override
	public boolean selectProperty(String propertyName, JsonElement value) {
		return getName().equals(propertyName);
	}
	@Override
	public boolean selectArrayEntry(int index, JsonElement value) {
		return this.index == index;
	}
}
