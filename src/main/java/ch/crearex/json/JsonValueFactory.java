package ch.crearex.json;

import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.JsonDomValueContextFactory;
import ch.crearex.json.dom.JsonNull;
import ch.crearex.json.dom.JsonNumber;
import ch.crearex.json.dom.JsonString;
import ch.crearex.json.dom.JsonBoolean;
import ch.crearex.json.impl.JsonBooleanValue;
import ch.crearex.json.impl.JsonNullValue;
import ch.crearex.json.impl.JsonNumberValue;
import ch.crearex.json.impl.JsonStringValue;
import ch.crearex.json.impl.JsonSimpleValueFactory;

/**
 * Simple Values are created by the API by using a value factory.
 * The default value factory is {@link JsonSimpleValueFactory}.
 * Simple values may be extended by extending the already existing
 * implementations:
 * <ul>
 * <li>{@link JsonStringValue} or {@link JsonString}</li>
 * <li>{@link JsonNumberValue} or {@link JsonNumber}</li>
 * <li>{@link JsonBooleanValue} or {@link JsonBoolean}</li>
 * <li>{@link JsonNullValue} or {@link JsonNull}</li>
 * </ul>
 * 
 * The {@link JsonDomBuilder} creates it own default factory, {@link JsonDomValueContextFactory},
 * and makes it available by {@link JsonDomBuilder#createJsonValueFactory(JsonContextBase)}.
 * If you like to change the value factory for the {@link JsonDomBuilder} you also have
 * to extend the {@link JsonDomBuilder} by overloading {@link JsonDomBuilder#createJsonValueFactory(JsonContextBase)}
 * 
 * @author Markus Niedermann
 *
 * @see JsonContext#setValueFactory(JsonValueFactory)
 */
public interface JsonValueFactory {
	JsonSimpleValue createStringValue(String value);
	JsonSimpleValue createNumberValue(String value);
	JsonSimpleValue createBooleanValue(boolean value);
	JsonSimpleValue createNullValue();
}
