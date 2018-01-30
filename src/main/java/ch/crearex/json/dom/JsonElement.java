package ch.crearex.json.dom;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonPath;

/**
 * Traversable interface to all JSON Elements.
 * @author Markus Niedermann
 * @see JsonDocument#traverse(JsonCallback)
 */
public interface JsonElement {
	void traverse(JsonDomContext context, JsonCallback callback);
	JsonPath getPath();
	String getTypeName();
}
