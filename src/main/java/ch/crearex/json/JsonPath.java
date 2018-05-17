package ch.crearex.json;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path to a JSON-Element e.g.
 * <pre>
 * /addr/1/city
 * 
 * for Geneva
 * 
 * { "addr": [{"city":"Bern"}, {"city":"Geneva"}]}
 * </pre>
 * 
 * Array Indices are 0-based.<br>
 * <br>
 * The String representation of a JsonPath contains /-characters as path separatos.
 * If you have to use a / as regular character for a property name, you have to escape
 * it by a ~ in the JsonPath!<br>
 * e.g. If a file path shall be a property name you have to write:
 * <pre>
 * Filesystem: C:/Data/Foo/Bar
 * The corresponding JsonPath entry: C:~/Data~/Foo~/Bar
 * </pre>
 * Use ~~ as escaped sequence for a single ~-character.
 * 
 * 
 * @author Markus Niedermann
 *
 */
public class JsonPath implements Iterable<Token> {
		
	public static final char ESCAPE_CHAR = JsonPathParser.ESCAPE_CHAR;
	public static final char SEPARATOR = JsonPathParser.DOT;
	
	private final LinkedList<Token> path;
	private static final JsonPathParser pathParser = new JsonPathParser();
	
	public JsonPath(Token[] path) {
		this.path = new LinkedList<Token>(Arrays.asList(path));
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
		this.path = new LinkedList<Token>();
	}

	public JsonPath add(Token entry) {
		this.path.add(entry);
		return this;
	}
	
	public Token removeLast() {
		return this.path.removeLast();
	}
	
	public Token getLast() {
		return this.path.getLast();
	}
	
	public Token getFirst() {
		return this.path.getFirst();
	}
	
	public JsonPath addFirst(Token entry) {
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
		for(Token entry: path) {
			if(first) {
				builder.append(entry.toString());
				first = false;
			} else {
				if(entry instanceof IndexToken) {
					builder.append(JsonPathParser.BRACKET_BEGIN);
				} else {
					builder.append(SEPARATOR);
				}
				
				builder.append(entry.toString());
				
				if(entry instanceof IndexToken) {
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
	public Iterator<Token> iterator() {
		return path.iterator();
	}

	public boolean isLastEntry(Token entry) {
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
