package ch.crearex.json.impl;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.dom.JsonDomValueFactory;

/**
 * Value Factory which always creates DOM-Values.
 * @author Markus Niedermann
 *
 */
public class CrearexJsonDomValueParserFactroy extends CrearexJsonParserFactory {

	public CrearexJsonDomValueParserFactroy() {
	}
	
	@Override
	public JsonValueFactory createValueFactory(JsonContext context) {
		return new JsonDomValueFactory(context);
	}
}
