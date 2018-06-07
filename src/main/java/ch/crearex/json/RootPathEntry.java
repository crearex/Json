package ch.crearex.json;

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
}
