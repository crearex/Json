package ch.crearex.json.test.schema;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.TestUtil;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.SchemaValidationStatus;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;
import ch.crearex.json.schema.JsonSchemaValidationException;

public class TestJsonSchema {
	private final JsonParserFactory parserFactory = new CrearexJsonParserFactory();
	private final JsonDomBuilder domBuilder = new JsonDomBuilder();
	
	private String result = "";
	
	private JsonSchemaCallback schemaCallback = new JsonSchemaCallback() {
		@Override
		public void schemaViolation(JsonSchemaValidationException violation) {
			result = "[" + violation.getPath() + "] " + violation.getMessage() + "\r\n";
		}
	};
	
	@Before
	public void before() {
		result = "";
	}
	
	@Test
	public void testSimple() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testSimpleNull() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"weight\":80,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
		
		text = "{\"name\":\"Felix\",\"age\":25,\"weight\":null,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
		
		text = "{\"name\":\"Felix\",\"age\":25,\"weight\":\"80\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testSimpleNull2() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"weight\":null,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testSimpleNull3() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"weight\":\"80\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testObjectNull() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":null,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testEnum() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":\"blue\"},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testEnum2() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":123},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testEnum3() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":null},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testConstOk() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":\"blue\", \"doors\":5},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testConstWrongValue() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":\"blue\", \"doors\":3},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testConstWrongType() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"car\":{\"color\":\"blue\", \"doors\":\"5\"},\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testIgnoreUnknownNumberProperty() throws Exception {
		// height is undefined
		String text = "{\"name\":\"Felix\", \"height\":185, \"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testIgnoreUnknownObjectProperty() throws Exception {
		// body is undefined
		String text = "{\"name\":\"Felix\", \"body\":{\"height\":185}, \"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testIgnoreUnknownArrayProperty() throws Exception {
		// body is undefined
		String text = "{\"name\":\"Felix\", \"numbers\":[1,2,3], \"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
	}
	
	@Test
	public void testVariableType() throws Exception {
		String text = "{\"id\":\"hello\",\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
		assertThat(domBuilder.getDocument().getRootObject().getString("id"), is("hello"));
		
		text = "{\"id\":123,\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		parser.parse(text);
		assertThat(result.isEmpty(), is(true));
		assertThat(domBuilder.getDocument().getRootObject().getInteger("id"), is(123));
		
		text = "{\"id\":false,\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
		result = "";
		
		text = "{\"id\":null,\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		parser.parse(text);
		assertThat(result.isEmpty(), is(false));
		result = "";
	}
	
	@Test
	public void testSimpleMissingMandatory() throws Exception {
		String text = "{\"name\":\"Felix\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		//System.out.println(result);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test
	public void testWrongType() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":\"25\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		JsonParser parser = parserFactory.createJsonParser(domBuilder, TestUtil.readResource("/json-schema.json"), schemaCallback);
		parser.parse(text);
		//System.out.println(result);
		assertThat(result.isEmpty(), is(false));
	}
	
	@Test 
	public void testJsonDocumentValidationValid() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/json-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testJsonDocumentValidationInvalid() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":\"25\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/json-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.INVALID));			
	}
}
