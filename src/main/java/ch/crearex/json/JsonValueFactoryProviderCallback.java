package ch.crearex.json;

/**
 * A {@link JsonCallback} which provides a {@link JsonValueFactory} to
 * be used by the JSON Parser.
 * 
 * @author Markus Niedermann
 *
 */
public interface JsonValueFactoryProviderCallback extends JsonCallback {
	JsonContextBase createContext(JsonCallback callback);
	JsonValueFactory createJsonValueFactory(JsonContextBase context);
}
