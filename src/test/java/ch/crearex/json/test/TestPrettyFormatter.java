package ch.crearex.json.test;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPrettyFormatter;
import ch.crearex.json.impl.CrearexJsonParserFactory;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

public class TestPrettyFormatter {
	
	private JsonPrettyFormatter formatter;
	private JsonParser parser;
	private StringBuilder result;
	
	private void clearResult() {
		if(result == null) {
			return;
		}
		result.delete(0, result.length());
	}
	
	@Before
	public void before() {
		CrearexJsonParserFactory factory = new CrearexJsonParserFactory();
		result = new StringBuilder();
		formatter = new JsonPrettyFormatter(result);
		parser = factory.createJsonParser(formatter);
	}
	
	@Test
	public void testSimpleObject() {
		String text = "{\"name\":\"Markus\"}";
		formatter.setCompressed();
		parser.parse(text);
		assertThat(result.toString(), is(text));
		
		clearResult();
		formatter.setPretty();
		formatter.setIdent(2);
		parser.parse(text);
		String expected = "{\r\n"+
				  		  "  \"name\": \"Markus\"\r\n"+
				          "}";
		assertThat(result.toString(), is(expected));
	}
	
	@Test
	public void testSimpleArray() {
		String text = "[true,false,null]";
		formatter.setCompressed();
		parser.parse(text);
		assertThat(result.toString(), is(text));
		
		clearResult();
		formatter.setPretty();
		formatter.setIdent(2);
		parser.parse(text);
		String expected = "[\r\n"+
				  		  "  true,\r\n"+
				  		  "  false,\r\n"+
				  		  "  null\r\n"+
				          "]";
		assertThat(result.toString(), is(expected));
	}

	@Test
	public void testComplex() {
		String text = "{\"greeting\":\"Hello World!\",\"numbers\":[1,2,3],\"child\":{\"name\":\"Markus\","+
				  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"},true,false],\"ort\":\"St. Gallen\"}}";
		formatter.setCompressed();
		parser.parse(text);
		assertThat(result.toString(), is(text));
		
		clearResult();
		formatter.setPretty();
		formatter.setIdent(2);
		parser.parse(text);
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
		assertThat(result.toString(), is(expected));
	}
}
