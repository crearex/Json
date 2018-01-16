package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonParser;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestJsonParser {
	
	private CrearexJsonParserFactory factory = new CrearexJsonParserFactory();
	private JsonParser json;
	private String result = "";
	private ArrayList<Integer> levels = new ArrayList<Integer>();
	
	@Before
	public void before() {
		levels.clear();
		json = factory.createJsonParser(new JsonCallback(){

			@Override
			public void beginObject(JsonContext context) {
				result += "{";
				context.getLevel();
				levels.add(context.getLevel());
			}

			@Override
			public void endObject(JsonContext context) {
				result += "}";
				levels.add(context.getLevel());
			}

			@Override
			public void beginArray(JsonContext context) {
				result += "[";
				levels.add(context.getLevel());
			}

			@Override
			public void endArray(JsonContext context) {
				result += "]";
				levels.add(context.getLevel());
			}

			@Override
			public void property(JsonContext context, String propertyName) {
				result += "(p="+propertyName+")";
				levels.add(context.getLevel());
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
				if(value.isBoolean()) {
					result += "("+value.asString()+")";
				} else if(value .isString()) {
					result += "('"+value+"')";
				} else if(value.isNumber()) {
					result += "("+value+")";
				} else if(value.isNull()) {
					result += "(null)";
				}
				levels.add(context.getLevel());
			}

			@Override
			public void comma(JsonContext context) {				
			}});
	}
	
	@Test
	public void testEmptyObject() {
		String text = "{}";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = " {}";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = "{} ";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = " {} ";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = "{ }";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = " { }";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = "{ } ";
		json.parse(text);
		assertThat(result, is("<{}>"));
		
		result = "";
		text = " { } ";
		json.parse(text);
		assertThat(result, is("<{}>"));
	}
	
	@Test
	public void testEmptyArray() {
		String text = "[]";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = " []";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = "[] ";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = " [] ";
		json.parse(text);
		assertThat(result, is("<[]>"));

		result = "";
		text = "[ ]";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = " [ ]";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = "[ ] ";
		json.parse(text);
		assertThat(result, is("<[]>"));
		
		result = "";
		text = " [ ] ";
		json.parse(text);
		assertThat(result, is("<[]>"));
	}

	@Test
	public void testSimpleObject() {
		String text = "{\"name\":\"Markus\"}";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" }";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\" }";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\",\"city\":\"Zürich\"}";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testSimpleObjectStream() {
		String text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\",\"city\":\"Z\\u00FCrich\"}";
		ByteArrayInputStream is = new ByteArrayInputStream(text.getBytes());
		json.parse(is);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testSimpleObjectReader() {
		String text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\",\"city\":\"Zürich\"}";
		StringReader reader = new StringReader(text);
		json.parse(reader);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testSimpleObjectOutputStream() throws IOException {
		String text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\",\"city\":\"Zürich\"}";
		char[] textArr = text.toCharArray();
		OutputStream os = json.toOutputStream();
		for(char ch: textArr) {
			os.write(ch);
		}
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testSimpleObjectFile() throws IOException {
		String text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\", \"city\":\"Zürich\"}";
		File file = File.createTempFile("JSON-Test", ".json");
		try {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				writer.write(text);
			}
			json.parse(file);
		} finally {
			file.delete();
		}
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testSimpleObjectChunks() {
		String text = "{ \"name\" :  \"Markus\" }";
		json.parse("{ \"n");
		json.parse("am");
		json.parse('e');
		json.parse('"');
		json.parse(' ');
		json.parse(':');
		json.parse(' ');
		json.parse(' ');
		json.parse(' ');
		json.parse('"');
		json.parse("Markus");
		json.parse("\"");
		json.parse(" }");
		assertThat(result, is("<{(p=name)('Markus')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" }";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\" }";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')}>"));
		
		result = "";
		text = "{ \"name\" : \"Markus\" ,\n \"nachname\" : \"Niedermann\",\"city\":\"Zürich\"}";
		json.parse(text);
		assertThat(result, is("<{(p=name)('Markus')(p=nachname)('Niedermann')(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testDataTypes() {
		String text = "{\"NULL\":null , \"TRUE\" : true,\"FALSE\":false , \"NUM\":1.234}";
		json.parse(text);
		assertThat(result, is("<{(p=NULL)(null)(p=TRUE)(true)(p=FALSE)(false)(p=NUM)(1.234)}>"));
	}
	
	@Test
	public void testNumber() {
		String text = "{\"NUM\":+1.234e5}";
		json.parse(text);
		assertThat(result, is("<{(p=NUM)(+1.234e5)}>"));
	}
	
	@Test
	public void testObjectProperty() {
		String text = "{\"person\": {\"name\":\"Markus\"}}";
		json.parse(text);
		assertThat(result, is("<{(p=person){(p=name)('Markus')}}>"));
		
		result = "";
		text = "{\"car\":\"VW\", \"person\": {\"name\":\"Markus\"}, \"city\":\"Zürich\"}";
		json.parse(text);
		assertThat(result, is("<{(p=car)('VW')(p=person){(p=name)('Markus')}(p=city)('Zürich')}>"));
	}
	
	@Test
	public void testArrayProperty() {
		String text = "{\"test\": [1.234, \"Markus\"]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[(1.234)('Markus')]}>"));
		
		result = "";
		text = "{\"test\": [1.234, \"Markus\", \"Niedermann\"]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[(1.234)('Markus')('Niedermann')]}>"));
		
		result = "";
		text = "{\"test\": [1.234, \"Markus\", \"Niedermann\", true, false, null]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[(1.234)('Markus')('Niedermann')(true)(false)(null)]}>"));
	}
	
	@Test
	public void testArrayPropertyWithObjectsEntries() {
		String text = "{\"test\": [{}]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[{}]}>"));
		
		result = "";
		text = "{\"test\": [{\"name\":\"Markus\"}]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[{(p=name)('Markus')}]}>"));
		
		result = "";
		text = "{\"test\": [{\"name\":\"Markus\", \"zahl\":-123}]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[{(p=name)('Markus')(p=zahl)(-123)}]}>"));
		
		result = "";
		text = "{\"test\": [{\"name\":\"Markus\", \"Liste\":[1,2,3], \"zahl\":-123}]}";
		json.parse(text);
		assertThat(result, is("<{(p=test)[{(p=name)('Markus')(p=Liste)[(1)(2)(3)](p=zahl)(-123)}]}>"));
	}
	
	@Test
	public void testStringContent() {
		String text = "[\"a\\\"b\\\\c\\t\\b\\f\\r\\n/e\\/f\\u002Fg\"]";
		String expected = "<[('a\"b\\c\t\b\f\r\n/e/f/g')]>";
		json.parse(text);
		assertThat(result, is(expected));
	}
	
	@Test
	public void testStringWithSpaces() {
		String text = "[\"Hello World!\"]";
		String expected = "<[('Hello World!')]>";
		json.parse(text);
		assertThat(result, is(expected));
	}
	
	@Test
	public void testLevel() {
		String text = "{\"a\":true,\"b\":{\"c\":null}, \"e\":[{\"f\":{}},true]}";
		json.parse(text);
		int index = 0;
		assertThat(levels.get(index++), is(1)); // {
		assertThat(levels.get(index++), is(1)); // a
		assertThat(levels.get(index++), is(1)); // true
		assertThat(levels.get(index++), is(1)); // b
		assertThat(levels.get(index++), is(2)); // {
		assertThat(levels.get(index++), is(2)); // c
		assertThat(levels.get(index++), is(2)); // null
		assertThat(levels.get(index++), is(2)); // }
		assertThat(levels.get(index++), is(1)); // e
		assertThat(levels.get(index++), is(2)); // [
 		assertThat(levels.get(index++), is(3)); // {
 		assertThat(levels.get(index++), is(3)); // f
 		assertThat(levels.get(index++), is(4)); // {
 		assertThat(levels.get(index++), is(4)); // }
		assertThat(levels.get(index++), is(3)); // }
		assertThat(levels.get(index++), is(2)); // true 
		assertThat(levels.get(index++), is(2)); // ]
		assertThat(levels.get(index++), is(1)); // }
	}

	
}
