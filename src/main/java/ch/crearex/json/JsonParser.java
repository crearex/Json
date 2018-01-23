package ch.crearex.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;

import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.impl.Source;


/**
 * JSON Parser API.
 * Use {@link JsonParserFactory#createJsonParser(JsonCallback)} to create a new JSON Parser.
 * Call one of the {@link JsonParser#parse(String) parse()} methods to feed the parser with data.
 * A JSON Document can be parsed by many {@link JsonParser#parse(String) parse()} calls.
 * It's not required, that the complete JSON Document is processed by a single 
 * {@link JsonParser#parse(String) parse()} call.
 * JSON-Tokens are notified by the {@link JsonCallback}. Use a JsonDomBuilder
 * as callback to create a simple JSON-DOM.
 * 
 * @author Markus Niedermann
 * @see JsonParserFactory
 * @see JsonDomBuilder
 */
public interface JsonParser {

	char OBJECT_BEGIN = '{';
	char OBJECT_END = '}';
	char ARRAY_BEGIN = '[';
	char ARRAY_END = ']';
	char QUOTE = '"';
	char EQUAL = '=';
	char COLON = ':';
	char COMMA = ',';
	char DOT = '.';
	char ESCAPE = '\\';
	
	String JSON_FALSE = "false";
	String JSON_TRUE = "true";
	String JSON_NULL = "null";
	
	String CHARSET_NAME = "UTF-8";
	
	HashSet<Character> WHITESPACE_CHARS = new HashSet<Character>(
			Arrays.asList(new Character[] { ' ', '\t', '\r', '\n' }));
	
	HashSet<Character> EOL_CHARS = new HashSet<Character>(Arrays.asList(new Character[] { '\r', '\n' }));
		
	HashSet<Character> NUMBER_CHARS = new HashSet<Character>(Arrays
			.asList(new Character[] { '-', '+', '.', 'e', 'E', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }));
	

	/**
	 * @param resolveStrings Set to True if escaped characters shall be resolved by {@link JsonSimpleValue#asString()}  (Default: True).
	 */
	JsonParser setResolveStrings(boolean resolveStrings);
	
	/**
	 * Returns True if escaped characters are resolved by {@link JsonSimpleValue#asString()} (Default: True).
	 */
	boolean isResolveStrings();

	/**
	 * Reset the parser to initial state.
	 * The {@link JsonParser#setResolveStrings(boolean) resolve String}
	 * setting remains unchanged.
	 */
	JsonParser reset();
	
	JsonParser parse(File file);
	JsonParser parse(InputStream source);
	JsonParser parse(Reader source);
	JsonParser parse(String text);
	JsonParser parse(char[] buffer);
	JsonParser parse(char[] buffer, int index, int length);
	JsonParser parse(char ch);
	
	/**
	 * Returns an OutputStream as source for the parser.
	 */
	OutputStream toOutputStream();
	
	JsonContext getContext();
	
	/**
	 * Clears all remaining characters (remaining because of an exception).
	 */
	JsonParser clear();

}