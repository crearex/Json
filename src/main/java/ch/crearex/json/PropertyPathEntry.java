package ch.crearex.json;

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
	
	public String getName() {
		String name = super.getName();
		if(name.indexOf(JsonPathParser.ESCAPE_CHAR)<0) {
			return name;
		}
		name = name.replace(""+ ESCAPE_CHAR + SEPARATOR, "" + SEPARATOR);
		name = name.replace("" + ESCAPE_CHAR + ESCAPE_CHAR, "" + ESCAPE_CHAR);
		return name;
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
}
