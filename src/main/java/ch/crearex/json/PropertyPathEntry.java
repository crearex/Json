package ch.crearex.json;

import ch.crearex.json.dom.JsonElement;

/**
 * Matches a property.
 * @author Markus Niedermann
 *
 */
public class PropertyPathEntry extends JsonPathEntry {
	
	static final char ESCAPE_CHAR = JsonPathParser.ESCAPE_CHAR;
	static final char SEPARATOR = JsonPathParser.DOT;
	
	public PropertyPathEntry() {
		super();
	}
	public PropertyPathEntry(String name) {
		super(escapeDotSyntaxName(name));
	}
	
	static String escapeDotSyntaxName(String entry) {
		if(entry.indexOf(JsonPathParser.ESCAPE_CHAR)<0 && entry.indexOf(SEPARATOR)<0) {
			return entry;
		}
		
		entry = entry.replace(""+ ESCAPE_CHAR,  "" + ESCAPE_CHAR + ESCAPE_CHAR);
		entry = entry.replace("" + SEPARATOR, ""+ ESCAPE_CHAR + SEPARATOR);
		return entry;
	}
	
	static String unexcapeDotSyntxName(String entry) {
		if(entry.indexOf(JsonPathParser.ESCAPE_CHAR)<0) {
			return entry;
		}
		entry = entry.replace(""+ ESCAPE_CHAR + SEPARATOR, "" + SEPARATOR);
		entry = entry.replace("" + ESCAPE_CHAR + ESCAPE_CHAR, "" + ESCAPE_CHAR);
		return entry;
	}
	
	public String getName() {
		return unexcapeDotSyntxName(super.getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PropertyPathEntry)) {
            return false;
		}
		PropertyPathEntry comp = (PropertyPathEntry) obj;
		String compName = comp.getName();
		if(compName != null) {
			return compName.equals(getName());
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return 71 * getName().hashCode();
	}
	
	@Override
	public boolean selectProperty(String propertyName, JsonElement value) {
		return getName().equals(propertyName);
	}
	@Override
	public boolean selectArrayEntry(int index, JsonElement value) {
		return false;
	}
}
