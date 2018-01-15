package ch.crearex.json.test.dom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.impl.CrearexJsonParserFactory;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonSimpleValue;

public class TestJsonDomTraverser {
	
	private CrearexJsonParserFactory jsonFactory;
	private static final String EOL = "\r\n";
	private String result = "";
	private ArrayList<String> pathList = new ArrayList<String>();
	
	private JsonCallback callback = new JsonCallback(){

		@Override
		public void beginObject(JsonContext context) {
			result += "{";
			pathList.add(context.getPath().toString());
		}

		@Override
		public void endObject(JsonContext context) {
			result += "}";
			pathList.add(context.getPath().toString());
		}

		@Override
		public void beginArray(JsonContext context) {
			result += "[";
			pathList.add(context.getPath().toString());
		}

		@Override
		public void endArray(JsonContext context) {
			result += "]";
			pathList.add(context.getPath().toString());
		}

		@Override
		public void property(JsonContext context, String propertyName) {
			result += "(p="+propertyName+")";
			pathList.add(context.getPath().toString());
		}

		@Override
		public void beginDocument(JsonContext context) {
			result += "<";
		}

		@Override
		public void endDocument(JsonContext context) {
			result += ">";
		}

		@Override
		public void simpleValue(JsonContext context, JsonSimpleValue value) {
			pathList.add(context.getPath().toString());
			if(value.isBoolean()) {
				result += "("+value.asString()+")";
			} else if(value.isString()) {
				result += "('"+value+"')";
			} else if(value.isNumber()) {
				result += "("+value+")";
			} else if(value.isNull()) {
				result += "(null)";
			}
		}

		@Override
		public void comma(JsonContext context) {			
		}};
	
	@Before
	public void before() {
		result = "";
		jsonFactory = new CrearexJsonParserFactory();
	}
	
	@Test
	public void testEmptyObject() {
		String text = "{}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(2));
		assertThat(result, is("<{}>"));
	}
	
	@Test
	public void testOneProperty() {
		String text = "{\"a\":true}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/a"));
		assertThat(pathList.get(index++), is("/a"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(4));
		assertThat(result, is("<{(p=a)(true)}>"));
	}
	
	@Test
	public void testTwoProperty() {
		String text = "{\"a\":true,\"b\":false}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/a"));
		assertThat(pathList.get(index++), is("/a"));
		assertThat(pathList.get(index++), is("/b"));
		assertThat(pathList.get(index++), is("/b"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(6));
		assertThat(result, is("<{(p=a)(true)(p=b)(false)}>"));
	}
	
	@Test
	public void testEmptyArray() {
		String text = "[]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(2));
		assertThat(result, is("<[]>"));
	}
	
	@Test
	public void testOneArrayEntry() {
		String text = "[true]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/0"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(3));
		assertThat(result, is("<[(true)]>"));
	}
	
	@Test
	public void testTwoArrayEntry() {
		String text = "[true, \"a\"]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/0"));
		assertThat(pathList.get(index++), is("/1"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(4));
		assertThat(result, is("<[(true)('a')]>"));
	}
	
	@Test
	public void testComplex() {
		String text = "{" +
				"\"name\":\"Niedermann\"," +
				"\"vorname\":\"Markus\"," +
				"\"adresse\":{" +
				    "\"strasse\":\"Musterweg 1\"," +
					"\"ort\":\"St. Gallen\","  +
					"\"plz\":9016" + 
				"}," + 
				"\"aktiv\":true," +
				"\"books\":[" +
				"\"Domain Driven Design\"," +
				"\"Design Patterns\"," +
				"{\"name\":\"Tiptopf\",\"ISBN\":\"123456789\"}"+"]" +
	 	    "}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		doc.traverse(callback);
		int index = 0;
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.get(index++), is("/name"));
		assertThat(pathList.get(index++), is("/name"));
		assertThat(pathList.get(index++), is("/vorname"));
		assertThat(pathList.get(index++), is("/vorname"));
		assertThat(pathList.get(index++), is("/adresse"));
		assertThat(pathList.get(index++), is("/adresse"));
		assertThat(pathList.get(index++), is("/adresse/strasse"));
		assertThat(pathList.get(index++), is("/adresse/strasse"));
		assertThat(pathList.get(index++), is("/adresse/ort"));
		assertThat(pathList.get(index++), is("/adresse/ort"));
		assertThat(pathList.get(index++), is("/adresse/plz"));
		assertThat(pathList.get(index++), is("/adresse/plz"));
		assertThat(pathList.get(index++), is("/adresse"));
		assertThat(pathList.get(index++), is("/aktiv"));
		assertThat(pathList.get(index++), is("/aktiv"));
		assertThat(pathList.get(index++), is("/books"));
		assertThat(pathList.get(index++), is("/books"));
		assertThat(pathList.get(index++), is("/books/0"));
		assertThat(pathList.get(index++), is("/books/1"));
		assertThat(pathList.get(index++), is("/books/2"));
		assertThat(pathList.get(index++), is("/books/2/name"));
		assertThat(pathList.get(index++), is("/books/2/name"));
		assertThat(pathList.get(index++), is("/books/2/ISBN"));
		assertThat(pathList.get(index++), is("/books/2/ISBN"));
		assertThat(pathList.get(index++), is("/books/2"));
		assertThat(pathList.get(index++), is("/books"));
		assertThat(pathList.get(index++), is("/"));
		assertThat(pathList.size(), is(28));
		//assertThat(result, is("<[]>"));
	}
	
	@Test
	public void testPrettyFormat() {
		String text = "{\"greeting\":\"Hello World!\",\"numbers\":[1,2,3],\"child\":{\"name\":\"Markus\","+
				  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"},true,false],\"ort\":\"St. Gallen\"}}";
		
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		
		StringBuilder formattedText = new StringBuilder();
		JsonPrettyFormatter formatter = new JsonPrettyFormatter(formattedText);
		formatter.setCompressed();
		doc.traverse(formatter);
		assertThat(formattedText.toString(), is(text));
		
		formattedText.delete(0, formattedText.length());
		formatter.setPretty();
		formatter.setIdent(2);
		doc.traverse(formatter);
		String expected = "{\r\n"+
		                  "  \"greeting\": \"Hello World!\",\r\n"+
				          "  \"numbers\":\r\n"+
		                  "  [\r\n"+
				          "    1,\r\n"+
		                  "    2,\r\n"+
				          "    3\r\n"+
		                  "  ],\r\n"+
				          "  \"child\":\r\n"+
		                  "  {\r\n"+
				          "    \"name\": \"Markus\",\r\n"+
				          "    \"streets\":\r\n"+
				          "    [\r\n"+
				          "      {\r\n"+
				          "        \"a\": \"x\"\r\n"+
				          "      },\r\n"+
				          "      {\r\n"+
				          "        \"b\": \"x\"\r\n"+
				          "      },\r\n"+
				          "      true,\r\n"+
				          "      false\r\n"+
				          "    ],\r\n"+
				          "    \"ort\": \"St. Gallen\"\r\n"+
				          "  }\r\n"+
				          "}";
		assertThat(formattedText.toString(), is(expected));
	}
}
