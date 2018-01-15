package ch.crearex.json;

import java.io.File;
import java.net.URL;

import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.schema.JsonSchemaValidationException;

/** 
 * Factory for creating JSON Parsers.
 * This Factory is used by {@link CrearexJson} or an individual
 * implementation for retrieving a parser.
 * 
 * @author Markus Niedermann
 *
 */
public interface JsonParserFactory {
	
	/**
	 * Implement your own {@link JsonCallback} or use a {@link JsonDomBuilder} for
	 * building a simple JSON DOM-Tree.
	 */
	JsonParser createJsonParser(JsonCallback callback);
	
	/**
	 * Implement your own {@link JsonCallback} or use a {@link JsonDomBuilder} for
	 * building a simple JSON DOM-Tree.
	 * @param schemaCallback null for default JSON Schema Callback wich throw a {@link JsonSchemaValidationException}.
	 */
	JsonParser createJsonParser(JsonCallback callback, String jsonSchema, JsonSchemaCallback schemaCallback);
	
	/**
	 * Implement your own {@link JsonCallback} or use a {@link JsonDomBuilder} for
	 * building a simple JSON DOM-Tree.
	 * @param schemaCallback null for default JSON Schema Callback wich throw a {@link JsonSchemaValidationException}.
	 */
	JsonParser createJsonParser(JsonCallback callback, File jsonSchema, JsonSchemaCallback schemaCallback);
	
	/**
	 * Implement your own {@link JsonCallback} or use a {@link JsonDomBuilder} for
	 * building a simple JSON DOM-Tree.
	 * @param schemaCallback null for default JSON Schema Callback wich throw a {@link JsonSchemaValidationException}.
	 */
	JsonParser createJsonParser(JsonCallback callback, URL jsonSchemaOriginUrl, JsonSchemaCallback schemaCallback);
	
	/**
	 * Implement your own {@link JsonCallback} or use a {@link JsonDomBuilder} for
	 * building a simple JSON DOM-Tree.
	 * @param schemaCallback null for default JSON Schema Callback wich throw a {@link JsonSchemaValidationException}.
	 */
	JsonParser createJsonParser(JsonCallback callback, JsonSchema jsonSchema, JsonSchemaCallback schemaCallback);
		
	JsonSchema createJsonSchema(String jsonSchema);
	JsonSchema createJsonSchema(File jsonSchema);
	JsonSchema createJsonSchema(URL jsonSchemaOriginUrl);
	
}
