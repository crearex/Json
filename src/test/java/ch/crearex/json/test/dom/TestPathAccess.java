package ch.crearex.json.test.dom;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.dom.JsonContainer;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.JsonValue;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestPathAccess {
	
	private JsonDomBuilder domBuilder;
	private JsonParser json;
	
	@Before
	public void before() {
		CrearexJsonParserFactory factory = new CrearexJsonParserFactory();
		domBuilder = new JsonDomBuilder();
		json = factory.createJsonParser(domBuilder);
	}
	
	@Test
	public void testGetObjectProperty() {
		json.parse("{\"name\":\"Markus\", \"nachname\":\"Niedermann\"}");
		JsonDocument doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$.name")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$.nachname")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetObjectPropertyWithSeparatorInName() {
		json.parse("{\"x.y\":\"Markus\", \"q"+JsonPath.ESCAPE_CHAR+"x\":\"Niedermann\"}");
		JsonDocument doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$.x"+JsonPath.ESCAPE_CHAR+".y")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$.q"+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR+"x")).asString(), is("Niedermann"));
	
		json.parse("{\"x.y\":\"Markus\", \"q"+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR+"r\":\"Niedermann\"}");
		doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$.x"+JsonPath.ESCAPE_CHAR+".y")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$.q"+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR+"r")).asString(), is("Niedermann"));

	}
	
	@Test
	public void testGetObjectPropertyStartingWithSeparator() {
		json.parse("{\".y\":\"Markus\", \""+JsonPath.ESCAPE_CHAR+"x\":\"Niedermann\"}");
		JsonDocument doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$."+JsonPath.ESCAPE_CHAR+".y")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$."+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR+"x")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetObjectPropertyEndingWithSeparator() {
		json.parse("{\"x.\":\"Markus\", \"q"+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR+"\":\"Niedermann\"}");
		JsonDocument doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$.x"+JsonPath.ESCAPE_CHAR+".")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$.q"+JsonPath.ESCAPE_CHAR+JsonPath.ESCAPE_CHAR)).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetArrayEntry() {
		json.parse("[\"Markus\", \"Niedermann\"]");
		JsonDocument doc = domBuilder.getDocument();
		assertThat(doc.getValue(new JsonPath("$[0]")).asString(), is("Markus"));
		assertThat(doc.getValue(new JsonPath("$[1]")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetRelativeObjectProperty() {
		json.parse("[{\"name\":\"Markus\", \"nachname\":\"Niedermann\"}]");
		JsonDocument doc = domBuilder.getDocument();
		JsonContainer container = doc.getRootArray().getContainer(0);
		assertThat(container.getValue(new JsonPath("@.name")).asString(), is("Markus"));
		assertThat(container.getValue(new JsonPath("@.nachname")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetRelativeArrayEntry() {
		json.parse("[[\"Markus\", \"Niedermann\"]]");
		JsonDocument doc = domBuilder.getDocument();
		JsonContainer container = doc.getRootArray().getContainer(0);
		assertThat(container.getValue(new JsonPath("@[0]")).asString(), is("Markus"));
		assertThat(container.getValue(new JsonPath("@[1]")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetRootObjectProperty() {
		json.parse("[{\"name\":\"Markus\", \"nachname\":\"Niedermann\"}]");
		JsonDocument doc = domBuilder.getDocument();
		JsonContainer container = doc.getRootArray().getContainer(0);
		assertThat(container.getValue(new JsonPath("$[0].name")).asString(), is("Markus"));
		assertThat(container.getValue(new JsonPath("$[0].nachname")).asString(), is("Niedermann"));
	}
	
	@Test
	public void testGetRootArrayEntry() {
		json.parse("{\"a\":[\"Markus\", \"Niedermann\"]}");
		JsonDocument doc = domBuilder.getDocument();
		JsonContainer container = doc.getRoot().getContainer("a");
		assertThat(container.getValue(new JsonPath("$.a[0]")).asString(), is("Markus"));
		assertThat(container.getValue(new JsonPath("$.a[1]")).asString(), is("Niedermann"));
	}
	
	@Test 
	public void testComplex() {
		String text = "{\"greeting\":\"Hello World!\", \"numbers\": [1,2,3], \"child\":{\"name\":\"Markus\","+
				  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"}], \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		JsonContainer container = doc.getRoot().getContainer("child").getContainer("streets").getContainer(0);
		assertThat(container.getValue(new JsonPath("$.greeting")).asString(), is("Hello World!"));
		assertThat(container.getValue(new JsonPath("$.numbers[1]")).asInteger(), is(2));
		assertThat(container.getValue(new JsonPath("$.child.name")).asString(), is("Markus"));
		assertThat(container.getValue(new JsonPath("$.child.streets[0].a")).asString(), is("x"));	
	}
	
	@Test
	public void testInsert() {
		String text = "{\"greeting\":\"Hello World!\", \"numbers\": [1,2,3], \"child\":{\"name\":\"Markus\","+
				  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"}], \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		JsonDocument doc = domBuilder.getDocument();
		
		JsonValue value = new JsonValue("Sonne");
		JsonPath insertPath = new JsonPath("$.child.streets[0].c");
		doc.getRoot().add(insertPath, value);
		JsonPath valuePath = value.getPath();
		assertThat(valuePath, is(insertPath));
		
		value = new JsonValue("Mond");
		insertPath = new JsonPath("$.child.streets[2]");
		doc.getRoot().add(insertPath, value);
		valuePath = value.getPath();
		assertThat(valuePath, is(insertPath));
		
		value = new JsonValue("Pollux");
		insertPath = new JsonPath("$.child.stars");
		doc.getRoot().add(insertPath, value);
		valuePath = value.getPath();
		assertThat(valuePath, is(insertPath));
		
		value = new JsonValue("Erde");
		insertPath = new JsonPath("$.planet");
		doc.getRoot().add(insertPath, value);
		valuePath = value.getPath();
		assertThat(valuePath, is(insertPath));
		
		
		text = "[\"a\",\"b\"]";
		json.parse(text);
		doc = domBuilder.getDocument();
		
		value = new JsonValue("cc");
		insertPath = new JsonPath("$[2]");
		doc.getRoot().add(insertPath, value);
		valuePath = value.getPath();
		assertThat(valuePath, is(insertPath));
	}

}
