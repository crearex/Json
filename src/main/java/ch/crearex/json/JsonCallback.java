package ch.crearex.json;

import ch.crearex.json.impl.CrearexJsonParserFactory;

/**
 * Notifies about parsed JSON tokens:
 * <ul>
 * <li>JSON Document Begin</li>
 * <li>JSON Document End</li>
 * <li><b><code>{</code></b> Object Begin</li>
 * <li>Property Name</li>
 * <li><b><code>}</code></b> Object End</li>
 * <li><b><code>[</code></b> Array Begin</li>
 * <li><b><code>]</code></b> Array End</li>
 * <li><b><code>,</code></b> Comma</li>
 * <li><b><code>true false null number string</code></b> Simple Value</li>
 * </ul>
 * 
 * @see CrearexJsonParserFactory#createJsonParser(JsonCallback)
 * 
 * @author Markus Niedermann
 * 
 */
public interface JsonCallback {

	/**
	 * Called before the first { or [ of a JSON-Document is read.
	 */
	void beginDocument(JsonContext context);
	
	/**
	 * Called after the last } or ] of a JSON-Document is read.
	 */
	void endDocument(JsonContext context);
	
	/**
	 * When a { is read.
	 */
	void beginObject(JsonContext context);
	
	/**
	 * When a } is read.
	 */
	void endObject(JsonContext context);
	
	/**
	 * When a [ is read.
	 */
	void beginArray(JsonContext context);
	
	/**
	 * When a ] is read.
	 */
	void endArray(JsonContext context);
	
	/**
	 * When a propertyName is read (the property value is not yet read).
	 */
	void property(JsonContext context, String propertyName);
	
	/**
	 * When a simple property value <b>or</b> a simple array entry is read.
	 * @see JsonSimpleValue
	 */
	void simpleValue(JsonContext context, JsonSimpleValue value);

	/**
	 * When a , is read.
	 */
	void comma(JsonContext context);
}
