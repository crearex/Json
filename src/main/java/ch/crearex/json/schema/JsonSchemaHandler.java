package ch.crearex.json.schema;

import java.net.URL;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.schema.builder.BuilderContext;
import ch.crearex.json.schema.builder.ContainerType;
import ch.crearex.json.schema.builder.SchemaBuilder;
import ch.crearex.json.schema.builder.SchemaTypeMap;

public class JsonSchemaHandler implements JsonSchema {

	private final ContainerType schemaRootType;
	private final URL jsonSchemaOriginUrl;
	
	public JsonSchemaHandler(JsonDocument schemaDoc, URL jsonSchemaOriginUrl, SchemaTypeMap schemaTypeMap) {
		this.jsonSchemaOriginUrl = jsonSchemaOriginUrl;
		BuilderContext context = new BuilderContext(jsonSchemaOriginUrl, schemaTypeMap);
		SchemaBuilder schemaBuilder = new SchemaBuilder(context);
		this.schemaRootType = schemaBuilder.build(schemaDoc);
	}
	
	@Override
	public void beginDocument(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = new SchemaStack(schemaRootType, baseContext.getSchemaCallback(), jsonSchemaOriginUrl);
		baseContext.setSchemaStack(stack);
	}

	@Override
	public void endDocument(JsonContext context) {
	}

	@Override
	public void beginObject(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.beginObject(context);
	}

	@Override
	public void endObject(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.endObject(context);
	}

	@Override
	public void beginArray(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.beginArray(context);
	}

	@Override
	public void endArray(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.endArray(context);
	}

	@Override
	public void property(JsonContext context, String propertyName) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.setNextProperty(context, propertyName);
	}

	@Override
	public void simpleValue(JsonContext context, JsonSimpleValue value) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
		stack.validateSimpleValue(context, value);
	}

	@Override
	public void comma(JsonContext context) {		
	}

}
