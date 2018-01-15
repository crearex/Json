package ch.crearex.json;

import ch.crearex.json.dom.JsonDocument;

/**
 * Callback called after a {@link JsonDocument JSON-Document} is read.
 * @author Markus Niedermann
 *
 */
public interface JsonDomCallback {
	void onDocument(JsonDocument document);
}
