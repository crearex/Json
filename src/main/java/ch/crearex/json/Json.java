package ch.crearex.json;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;

/**
 * Simple JSON Parser Facade - use {@link CrearexJson} as implementation class:
 * <pre>
 * Json json = new CrearexJson();
 * </pre>
 * Wraps the most common use of the {@link JsonParser} and gives
 * an easy access to JSON by generating a {@link JsonDocument JSON Document Model}.
 * If you need a more customized JSON Parser use the {@link CrearexJsonParserFactory}
 * to create a {@link JsonParser}.
 * @author Markus Niedermann
 *
 */
public interface Json {

	String EOL = JsonPrettyFormatter.EOL;

	/**
	 * Set a JSON Schema: <a href="http://json-schema.org/" target="schema">http://json-schema.org/</a>
	 * @throws JsonSchemaException If there is an error in the JSON Schema
	 * @throws JsonSchemaValidationException If a schema violation occurred (use a {@link JsonSchemaCallback}) 
	 *                                       if you want control the behavior in case of a violation).
	 */
	Json setSchema(File jsonSchemaFile);


	/**
	 * Set a JSON Schema: <a href="http://json-schema.org/" target="schema">http://json-schema.org/</a>
	 * @throws JsonSchemaException If there is an error in the JSON Schema
	 */
	Json setSchema(URL jsonSchemaUrl);
	
	/**
	 * Set a JSON Schema: <a href="http://json-schema.org/" target="schema">http://json-schema.org/</a>
	 * @see JsonParserFactory#createJsonSchema(URL)
	 */
	Json setSchema(JsonSchema jsonSchema);
	
	/**
	 * Set a JSON Schema: <a href="http://json-schema.org/" target="schema">http://json-schema.org/</a>
	 * @throws JsonSchemaException If there is an error in the JSON Schema                                      
	 */
	Json setSchema(String jsonSchema);


	/**
	 * Set the {@link JsonSchemaCallback} to define how a schema violations must be processed.
	 * Per default, schema violations are reported by throwing a {@link JsonSchemaValidationException}
	 * out of the dfault {@link JsonSchemaCallback}.
	 * @param callback an own callback or null to force the default implementation.
	 */
	Json setSchemaCallback(JsonSchemaCallback callback);
	
	/**
	 * Set the {@link JsonDomCallback} to get informed by callback on successful parsed
	 * {@link JsonDocument JSON Documents}.
	 */
	Json setDomCallback(JsonDomCallback callback);
	
	/**
	 * Reset the underlying {@link JsonParser parser} to initial state.
	 * The {@link JsonParser#setResolveStrings(boolean) resolve String}
	 * setting remains unchanged.
	 */
	Json reset();

	JsonDocument parse(File file);

	JsonDocument parse(InputStream source);

	JsonDocument parse(Reader source);

	JsonDocument parse(String text);

}