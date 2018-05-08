package ch.crearex.json;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
public class JsonPath implements Iterable<JsonPathEntry> {
		
	/**
	 * The path separator is a slash (/)
	 */
	public static final char PATH_SEPARATOR = JsonPathParser.PATH_SEPARATOR;
	public static final char ESCAPE_CHAR = JsonPathParser.ESCAPE_CHAR;
	private final LinkedList<JsonPathEntry> path;
	private static final JsonPathParser pathParser = new JsonPathParser();
	
	
	
	public JsonPath(JsonPathEntry[] path) {
		this.path = new LinkedList<JsonPathEntry>(Arrays.asList(path));
	}
	
	public JsonPath(String path) {
		this.path = new LinkedList<JsonPathEntry>();
		if(path == null) {
			return;
		}
		path = path.trim();
		if(path.isEmpty()) {
			return;
		}
		boolean isRoot = path.charAt(0)==PATH_SEPARATOR;
		if((path.length() == 1) && isRoot) {
			add(JsonPathEntry.createEmptyEntry(isRoot));
			return;
		}
		List<String> entries = pathParser.parse(path);
		int index = -1;
		for(String entry: entries) {
			index++;
			if(entry == null) {
				throw new IllegalArgumentException("Create JSON Path for '"+path+"' failed! Illegal null entry.");
			}
			entry = entry.trim();
			if(entry.isEmpty()) {
				if(index == 0) {
					continue;
				}
				throw new IllegalArgumentException("Create JSON Path for '"+path+"' failed! Illegal empty entry.");
			}
			try {
				int arrayIndex = Integer.parseInt(entry);
				add(JsonPathEntry.createArrayEntry(arrayIndex, isRoot));
			} catch(NumberFormatException e) {
				add(JsonPathEntry.createObjectEntry(entry, isRoot));
			} catch(Exception e) {
				throw new IllegalArgumentException("Create JSON Path for '"+path+"' failed! " + e.getMessage(), e);
			}
			isRoot = false;
		}
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
		StringBuilder builder = new StringBuilder();
		int first = 1;
		// a path always begins with a PATH_SEPARATOR
		//builder.append(PATH_SEPARATOR);
		for(JsonPathEntry entry: path) {
			if(entry.isRoot()) {
				builder.append(PATH_SEPARATOR);
				if(!entry.hasData()) {
					first++;
				}
			}
			if(first>0) {
				first--;
			} else if(entry.hasData()){
				builder.append(PATH_SEPARATOR);
			}
			builder.append(escapeChars(entry.toString()));
			
		}
		return builder.toString();
	}
	
	private Object escapeChars(String entry) {
		if(entry.indexOf(JsonPathParser.ESCAPE_CHAR)<0 && entry.indexOf(PATH_SEPARATOR)<0) {
			return entry;
		}
		
		entry = entry.replace(""+JsonPathParser.ESCAPE_CHAR, ""+JsonPathParser.ESCAPE_CHAR + JsonPathParser.ESCAPE_CHAR);
		entry = entry.replace("" + PATH_SEPARATOR, ""+JsonPathParser.ESCAPE_CHAR + PATH_SEPARATOR);
		return entry;
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

	public String concat(String trailingPart) {
		String path = toString();
		if(path.lastIndexOf(PATH_SEPARATOR) == path.length()-1) {
			return path + trailingPart;
		} else {
			return path + PATH_SEPARATOR + trailingPart;
		}
	}

}
