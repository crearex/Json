package ch.crearex.json.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import ch.crearex.json.Json;
import ch.crearex.json.JsonBuilderException;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonDomCallback;
import ch.crearex.json.JsonException;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;

/**
 * Crearex Standard Implementation of the {@link Json JSON API}
 * @author Markus Niedermann
 *
 */
public class CrearexJson implements Json {

	private final JsonParserFactory parserFactory;
	private final JsonDomBuilder domBuilder;
	private final JsonParser parser;
	
	public CrearexJson() {
		this(new CrearexJsonParserFactory());
	}
	
	public CrearexJson(JsonParserFactory parserFactory) {
		this.parserFactory = parserFactory;
		this.domBuilder = new JsonDomBuilder();
		parser = parserFactory.createJsonParser(domBuilder);
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.impl.Json#setSchema(java.io.File, ch.crearex.json.JsonSchemaCallback)
	 */
	@Override
	public Json setSchema(File jsonSchemaFile) {
		JsonSchema jsonSchema = parserFactory.createJsonSchema(jsonSchemaFile);
		((JsonContextBase)parser.getContext()).setSchema(jsonSchema);
		return this;
	}
	
	@Override
	public Json setSchema(URL jsonSchemaUrl) {
		JsonSchema jsonSchema = parserFactory.createJsonSchema(jsonSchemaUrl);
		((JsonContextBase)parser.getContext()).setSchema(jsonSchema);
		return this;
	}
	
	@Override
	public Json setSchema(String jsonSchema) {
		JsonSchema schema = parserFactory.createJsonSchema(jsonSchema);
		((JsonContextBase)parser.getContext()).setSchema(schema);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.impl.Json#reset()
	 */
	@Override
	public Json reset() {
		parser.reset();
		return this;
		
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.impl.Json#parse(java.io.File)
	 */
	@Override
	public JsonDocument parse(File file) {
		parser.parse(file);
		return domBuilder.getDocument();
	}
	
	@Override
	public JsonDocument parse(URL url) {
		try(InputStream is = url.openStream()) {
			return parse(is);
		} catch (IOException e) {
			throw new JsonBuilderException("Parse URL '"+url+"' failed! " + e.getMessage(), e);
		}
	}
	
	@Override
	public JsonDocument parse(InputStream source) {
		parser.parse(source);
		return domBuilder.getDocument();
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.impl.Json#parse(java.io.Reader)
	 */
	@Override
	public JsonDocument parse(Reader source) {
		parser.parse(source);
		return domBuilder.getDocument();
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.impl.Json#parse(java.lang.String)
	 */
	@Override
	public JsonDocument parse(String text) {
		parser.parse(text);
		return domBuilder.getDocument();
	}
	
	@Override
	public Json setSchema(JsonSchema jsonSchema) {
		((JsonContextBase)parser.getContext()).setSchema(jsonSchema);
		return this;
	}

	@Override
	public Json setSchemaCallback(JsonSchemaCallback schemaCallback) {
		((JsonContextBase)parser.getContext()).setSchemaCallback(schemaCallback);
		return this;
	}

	@Override
	public Json setDomCallback(JsonDomCallback callback) {
		JsonDomBuilder builder;
		if(callback == null) {
			builder = new JsonDomBuilder();
		} else {
			builder = new JsonDomBuilder(callback);
		}
		parser.getContext().setJsonCallback(builder);
		return this;
	}

	@Override
	public void clear() {
		parser.clear();
	}
	
}
