package ch.crearex.json.test.dom;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonContainer;
import ch.crearex.json.JsonParser;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.JsonValue;
import ch.crearex.json.impl.CrearexJsonParserFactory;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.closeTo;

public class TestJsonDomBuilder {

	private CrearexJsonParserFactory jsonFactory;
	private static final String EOL = "\r\n";
	
	@Before
	public void before() {
		jsonFactory = new CrearexJsonParserFactory();
	}
	
	@Test
	public void testBuildEmptyObject() {
		String text = "{}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		assertThat(root instanceof JsonObject, is(true));
		JsonObject objRoot = document.getRootObject();
		assertThat(objRoot != null, is(true));
		assertThat(objRoot.isRoot(), is(true));
	}
	
	@Test
	public void testBuildEmptyArray() {
		String text = "[]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		assertThat(root instanceof JsonArray, is(true));
		JsonArray objRoot = document.getRootArray();
		assertThat(objRoot != null, is(true));
		assertThat(objRoot.isRoot(), is(true));
	}
	
	@Test
	public void testObjectStringValue() {
		String helloWorld = "Hello World!";
		String text = "{\"greet me\": \""+helloWorld+"\"}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonObject obj = document.getRootObject();
		assertThat(obj.size(), is(1));
		assertThat(obj.getValue("greet me").asString(), is(helloWorld));
	}
	
	@Test
	public void testObjectNumberValue() {
		String number = "1.234567";
		String text = "{\"num\": "+number+"}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonObject obj = document.getRootObject();
		assertThat(obj.size(), is(1));
		assertThat(obj.getValue("num").asDouble(), is(1.234567));
	}
	
	@Test
	public void testObjectBooleanValue() {
		String number = "1.234567";
		String text = "{\"flag\": true}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonObject obj = document.getRootObject();
		assertThat(obj.size(), is(1));
		assertThat(obj.getValue("flag").asBoolean(), is(true));
	}
	
	@Test
	public void testObjectNullValue() {
		String number = "1.234567";
		String text = "{\"flag\": null}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonObject obj = document.getRootObject();
		assertThat(obj.size(), is(1));
		assertThat(obj.getValue("flag").isNull(), is(true));
	}
	
	
	@Test
	public void testArrayStringValue() {
		String helloWorld = "Hello World!";
		String text = "[\""+helloWorld+"\"]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonArray arr = document.getRootArray();
		assertThat(arr.size(), is(1));
		assertThat(arr.getValue(0).toString(), is(helloWorld));
	}
	
	@Test
	public void testArrayNumberValue() {
		String number = "1.234";
		String text = "[\""+number+"\"]";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonArray arr = document.getRootArray();
		assertThat(arr.size(), is(1));
		assertThat(arr.getValue(0).toString(), is(number));
		assertThat(arr.getValue(0).asDouble(), is(1.234));
	}
	
	@Test
	public void testComplexJson() {
		String text = "{"+ EOL +
			"\"name\": \"Niedermann\", " + EOL +
			"\"vorname\": \"Markus\", " + EOL +
			"\"adresse\": {"+ EOL +
			    "\"strasse\":\"Musterweg 1\"," + EOL + EOL +
				"  \"ort\" : \"St. Gallen\" ,  " + EOL +
				"\"plz\": 9016" + EOL +
			"}, " + EOL + EOL + EOL +
			"\"aktiv\" : true," + EOL +
			"\"bücher\":["+ EOL +
			"       \"Domain Driven Design\"," +
			"       \"Design Patterns\"," +
			"{\"name\":\"Tiptopf\",\"ISBN\":\"123456789\"}"+"]  " + EOL +
 	    "}";
		JsonDomBuilder domBuilder = new JsonDomBuilder();
		JsonParser json = jsonFactory.createJsonParser(domBuilder);
		json.parse(text);
		JsonDocument document = domBuilder.getDocument();
		JsonContainer root = document.getRoot();
		JsonObject rootObj = document.getRootObject();
		assertThat(rootObj.size(), is(5));
		assertThat(rootObj.getValue("name").asString(), is("Niedermann"));
		assertThat(rootObj.getValue("name").isString(), is(true));
		assertThat(rootObj.getValue("vorname").asString(), is("Markus"));
		assertThat(rootObj.getValue("aktiv").asBoolean(), is(true));
		assertThat(rootObj.getValue("aktiv").isBoolean(), is(true));
		assertThat(rootObj.getValue("aktiv").isString(), is(false));
		JsonObject addresse = rootObj.getObject("adresse");
		assertThat(addresse.getValue("strasse").asString(), is("Musterweg 1"));
		assertThat(addresse.getValue("ort").asString(), is("St. Gallen"));
		assertThat(addresse.getValue("plz").asInteger(), is(9016));
		assertThat(addresse.getValue("plz").isNumber(), is(true));
		assertThat(addresse.getValue("plz").asString(), is("9016"));
		JsonArray books = rootObj.getArray("bücher");
		assertThat(books.size(), is(3));
		assertThat(books.getValue(0).asString(), is("Domain Driven Design"));
		assertThat(books.getValue(1).asString(), is("Design Patterns"));
		assertThat(books.getObject(2).getValue("name").asString(), is("Tiptopf"));
		JsonObject parent = books.getParentObject();
		assertThat(parent.getValue("name").asString(), is("Niedermann"));
		parent.add("alter", new JsonValue(44));
		assertThat(rootObj.size(), is(6));
		assertThat(rootObj.getValue("alter").asInteger(), is(44));	
	}
	
	@Test
	public void testToString() {
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
		String generated = domBuilder.getDocument().toString();
		assertThat(generated, is(text));
	}
}
