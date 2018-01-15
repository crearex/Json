package ch.crearex.json.schema;

import java.net.URL;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonDocument;

public class JsonSchemaImpl implements JsonSchema {

	private final SchemaType schemaRootType;
	private final URL jsonSchemaOriginUrl;
	
	public JsonSchemaImpl(JsonDocument schemaDoc, URL jsonSchemaOriginUrl) {
		this.jsonSchemaOriginUrl = jsonSchemaOriginUrl;
		BuilderContext context = new BuilderContext();
		SchemaBuilder schemaBuilder = new SchemaBuilder(context);
		schemaRootType = schemaBuilder.build(schemaDoc);
	}
	
	@Override
	public void beginDocument(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = new SchemaStack(schemaRootType, baseContext.getSchemaCallback(), jsonSchemaOriginUrl);
		baseContext.setSchemaStack(stack);
	}

	@Override
	public void endDocument(JsonContext context) {
		JsonContextBase baseContext = (JsonContextBase)context;
		SchemaStack stack = baseContext.getSchemaStack();
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
