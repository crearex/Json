package ch.crearex.json;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path to a JSON-Element e.g.
 * <pre>
 * $.addr[1].city
 * 
 * for Geneva
 * 
 * { "addr": [{"city":"Bern"}, {"city":"Geneva"}]}
 * </pre>
 * 
 * Array Indices are 0-based.<br>
 * <br>
 * The String representation of a JsonPath contains .-characters as path separators.
 * If you have to use a . as regular character for a property name, you have to escape
 * it by a \ in the JsonPath!<br>
 * Use \\ as escaped sequence for a single \-character.
 * 
 * 
 * @author Markus Niedermann
 *
 */
public class JsonPath implements Iterable<JsonPathEntry> {
		
	public static final char ESCAPE_CHAR = JsonPathParser.ESCAPE_CHAR;
	public static final char SEPARATOR = JsonPathParser.DOT;
	
	private final LinkedList<JsonPathEntry> path;
	private static final JsonPathParser pathParser = new JsonPathParser();
	
	public JsonPath(JsonPathEntry[] path) {
		this.path = new LinkedList<JsonPathEntry>(Arrays.asList(path));
	}
	
	public JsonPath(String path) {
		
		if(path == null) {
			throw new JsonPathIllegalSyntaxException("Illegal path argument! path must not be null!");
		}
		
		path = path.trim();
		if(path.isEmpty()) {
			throw new JsonPathIllegalSyntaxException("Illegal path argument! path must not be empty!");
		}
		
		this.path = pathParser.parseTokens(path);
	}

	public JsonPath() {
		this.path = new LinkedList<JsonPathEntry>();
	}

	public JsonPath add(JsonPathEntry entry) {
		this.path.add(entry);
		return this;
	}
	
	public JsonPathEntry removeLast() {
		return this.path.removeLast();
	}
	
	public JsonPathEntry getLast() {
		return this.path.getLast();
	}
	
	public JsonPathEntry getFirst() {
		return this.path.getFirst();
	}
	
	public JsonPath addFirst(JsonPathEntry entry) {
		this.path.addFirst(entry);
		return this;
	}
	
	public int size() {
		return this.path.size();
	}
	
	public JsonPath clear() {
		this.path.clear();
		return this;
	}
	
	@Override
	public String toString() {
		return toJsonPath();
		//return toSlashPath();
	}
	
	private String toJsonPath() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(JsonPathEntry entry: path) {
			if(first) {
				builder.append(entry.toString());
				first = false;
			} else {
				if(entry instanceof IndexPathEntry) {
					builder.append(JsonPathParser.BRACKET_BEGIN);
				} else {
					builder.append(SEPARATOR);
				}
				
				builder.append(entry.toString());
				
				if(entry instanceof IndexPathEntry) {
					builder.append(JsonPathParser.BRACKET_END);
				}
			}
		}
		return builder.toString();
	}
	
	

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonPath)) {
            return false;
		}
		JsonPath comp = (JsonPath) obj;
		return this.path.equals(comp.path);
	}
	
	@Override
	public int hashCode() {
		return this.path.hashCode();
	}

	@Override
	public Iterator<JsonPathEntry> iterator() {
		return path.iterator();
	}

	public boolean isLastEntry(JsonPathEntry entry) {
		return entry == path.getLast();
	}

	public String concatPropertyName(String trailingPart) {
		String path = toString();
		if(path.lastIndexOf(SEPARATOR) == path.length()-1) {
			return path + trailingPart;
		} else {
			return path + SEPARATOR + trailingPart;
		}
	}

}
