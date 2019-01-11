package ch.crearex.json.dom;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.impl.JsonBooleanValue;
import ch.crearex.json.impl.JsonNullValue;
import ch.crearex.json.impl.JsonNumberValue;
import ch.crearex.json.impl.JsonStringValue;

/**
 * The value factory which creates the {@link JsonSimpleValue}s for a JSON DOM Tree.
 * 
 * @author Markus Niedermann
 *
 */
public class JsonDomValueContextFactory extends JsonDomValueFactory {

	private final JsonDomContext context;
	
	public JsonDomValueContextFactory(JsonDomContext context) {
		super(context);
		this.context = context;
	}
	
	protected JsonDomContext getContext() {
		return context;
	}

}
