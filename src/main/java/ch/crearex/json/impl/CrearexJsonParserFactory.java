package ch.crearex.json.impl;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ch.crearex.json.JsonBuilderException;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonException;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.JsonValueFactoryProviderCallback;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaHandler;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.builder.SchemaTypeMap;

/**
 * Crearex default factory for creating JSON Parsers.
 * @author Markus Niedermann
 *
 */
public class CrearexJsonParserFactory implements JsonParserFactory {
	
	@Override
	public JsonParser createJsonParser(JsonCallback callback) {
		return doCreateParser(callback);
	}
	
	@Override
	public JsonParser createJsonParser(JsonCallback callback, String jsonSchema, JsonSchemaCallback schemaCallback) {
		JsonSchema schema = createJsonSchema(jsonSchema);
		JsonParser parser = createJsonParser(callback, schema, schemaCallback);
		return parser;
	}
	
	@Override
	public JsonParser createJsonParser(JsonCallback callback, File jsonSchema, JsonSchemaCallback schemaCallback) {
		JsonSchema schema = createJsonSchema(jsonSchema);
		return createJsonParser(callback, schema, schemaCallback);
	}

	@Override
	public JsonParser createJsonParser(JsonCallback callback, URL jsonSchemaOriginUrl, JsonSchemaCallback schemaCallback) {
		JsonSchema schema = createJsonSchema(jsonSchemaOriginUrl);
		JsonParser parser = createJsonParser(callback, schema, schemaCallback);
		return parser;
	}
	
	@Override
	public JsonParser createJsonParser(JsonCallback callback, JsonSchema jsonSchema, JsonSchemaCallback schemaCallback) {
		JsonParserImpl parser = doCreateParser(callback);
		parser.getContext().setSchema(jsonSchema).setSchemaCallback(schemaCallback);
		return parser;
	}

	@Override
	public JsonSchema createJsonSchema(String jsonSchemaContent) {	
		JsonDocument schemaDoc = doParseSchema(jsonSchemaContent);
		JsonSchema schema = null;
		schema = new JsonSchemaHandler(schemaDoc, null, new SchemaTypeMap());
		return schema;
	}
	
	@Override
	public JsonSchema createJsonSchema(File jsonSchema) {		
		try {
			URL url = jsonSchema.toURI().toURL();
			return createJsonSchema(url);
		} catch(JsonException e) {
			throw e;
		} catch(MalformedURLException e) {
			throw new JsonBuilderException("Parse JSON Schema '"+jsonSchema+"' failed! " + e.getMessage(), e);
		} catch(IllegalArgumentException e) {
			throw new JsonBuilderException("Parse JSON Schema '"+jsonSchema+"' failed! " + e.getMessage(), e);
		}
	}
	
	@Override
	public JsonSchema createJsonSchema(URL jsonSchemaOriginUrl) {
		JsonDocument schemaDoc = doParseSchema(jsonSchemaOriginUrl);		
		return new JsonSchemaHandler(schemaDoc, jsonSchemaOriginUrl, new SchemaTypeMap());
	}
	
	/**
	 * Returns a JsonDocument containing the JSON Schema.
	 */
	private JsonDocument doParseSchema(String jsonSchemaContent) {
		JsonDomBuilder builder = new JsonDomBuilder();
		JsonParser parser = createJsonParser(builder);
		parser.parse(jsonSchemaContent);
 		builder.getContext().validateStructure();
		return builder.getDocument();
	}
	
	/**
	 * Returns a JsonDocument containing the JSON Schema.
	 */
	private JsonDocument doParseSchema(URL jsonSchemaUrl) {
		JsonDomBuilder builder = new JsonDomBuilder();
		JsonParser parser = createJsonParser(builder);
		try(InputStream is = jsonSchemaUrl.openConnection().getInputStream()) {
			parser.parse(is);
		} catch (Exception e) {
			throw new JsonBuilderException("Parse JSON Schema '"+jsonSchemaUrl+"' failed! " + e.getMessage(), e);
		}
		builder.getContext().validateStructure();
		return builder.getDocument();
	}
	
	private JsonParserImpl doCreateParser(JsonCallback callback) {
		Source source = new Source()
				.setEndOfLineCharacters(JsonParser.EOL_CHARS)
				.setWhitespaceCharacters(JsonParser.WHITESPACE_CHARS);
		JsonContextBase context = null;
		if(callback instanceof JsonValueFactoryProviderCallback) {
			context = ((JsonValueFactoryProviderCallback)callback).createContext(callback);
			context.setValueFactory(((JsonValueFactoryProviderCallback)callback).createJsonValueFactory(context));
		} else {
			context = new JsonContextImpl(callback);
			context.setValueFactory(new JsonSimpleValueFactory(context));
		}
		return new JsonParserImpl(source, context);
	}

}
