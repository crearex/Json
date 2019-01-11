package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonDomCallback;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.JsonDomContext;
import ch.crearex.json.dom.JsonDomValueContextFactory;
import ch.crearex.json.dom.JsonString;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestJavaDoc {

	@Test
	public void testSimpleAccessHelloWorld() {
		String text = "{\"greeting\": \"Hello World!\"}";
		Json json = new CrearexJson();
		JsonDocument document = json.parse(text);
		String greeting = document.getRootObject().getString("greeting");
		// Output:
		// -------------------------
		// greeting = "Hello World!"
		
		assertThat("Hello World!", is(greeting));
	}
	
	@Test
	public void testCustomizedHelloWorld() {
		String text = "{\"greeting\": \"Hello World!\"}";
		
		JsonParserFactory factory = new CrearexJsonParserFactory();
		JsonDomCallback domCallback = new JsonDomCallback() {
			@Override
			public void onDocument(JsonDocument document) {
				System.out.println("NEW DOCUMENT: " + document);
			}
		};
		JsonDomBuilder domBuilder = new JsonDomBuilder(domCallback) {
			// Overwrite createContext() to customize the context
			public JsonContextBase createContext(JsonCallback callback) {
				return new JsonDomContext(callback);
			}
			public JsonValueFactory createJsonValueFactory(JsonContextBase context) {
				// You'll get your customized context
				JsonDomContext domContext = (JsonDomContext)context;
				// Customize the value Factory e.g.
				return new JsonDomValueContextFactory(domContext) {
					@Override
					public JsonSimpleValue createStringValue(String value) {
						return new JsonString(getContext(), value) {
							@Override
							public String asString() {
								String value = super.asString();
								return ":-) " + value;
							}
						};
					}
				};
			}
		};
		
		JsonParser parser = factory.createJsonParser(domBuilder);
		parser.parse(text);
		
		// Output:
		// -------------------------
		// NEW DOCUMENT: {"greeting":":-) Hello World!"}
		
		
		JsonDocument document = domBuilder.getDocument();
		String greeting = document.getRootObject().getString("greeting");
		assertThat(":-) Hello World!", is(greeting));
	}
	
	
	@Test
	public void testFundamentalJsonCallback() {
		JsonParserFactory factory = new CrearexJsonParserFactory();
		JsonCallback callback = new JsonCallback() {
			
			@Override
			public void beginDocument(JsonContext context) {
				System.out.println("BEGIN DOCUMENT");
			}
			
			@Override
			public void endDocument(JsonContext context) {
				System.out.println("END DOCUMENT");
			}

			@Override
			public void beginObject(JsonContext context) {
				System.out.println("BEGIN OBJECT");
			}

			@Override
			public void endObject(JsonContext context) {
				System.out.println("END OBJECT");
			}

			@Override
			public void beginArray(JsonContext context) {
				System.out.println("BEGIN ARRAY");
			}

			@Override
			public void endArray(JsonContext context) {
				System.out.println("END ARRAY");
			}

			@Override
			public void property(JsonContext context, String propertyName) {
				System.out.println("PROPERTY: " + propertyName);
			}

			@Override
			public void simpleValue(JsonContext context, JsonSimpleValue value) {
				System.out.println("VALUE: " + value);
			}

			@Override
			public void comma(JsonContext context) {
				System.out.println("COMMA");
			}};
			
		JsonParser parser = factory.createJsonParser(callback);
		
		String text = "{\"greeting\": \"Hello World!\"}";
		parser.parse(text);
		// Output:
		// -------------------------
		// BEGIN DOCUMENT
		// BEGIN OBJECT
		// PROPERTY: greeting
		// VALUE: Hello World!
		// END OBJECT
		// END DOCUMENT
	}
}
