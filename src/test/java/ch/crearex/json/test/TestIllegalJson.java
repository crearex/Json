package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonIllegalSyntaxException;
import ch.crearex.json.JsonParser;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestIllegalJson {

	private CrearexJsonParserFactory factory = new CrearexJsonParserFactory();
	private JsonParser json;
	private String result = "";
	private boolean exceptionCaught = false;
	
	@Before
	public void before() {
		exceptionCaught = false;
		json = factory.createJsonParser(new JsonCallback(){

			@Override
			public void beginObject(JsonContext context) {
				result += "{";
			}

			@Override
			public void endObject(JsonContext context) {
				result += "}";
			}

			@Override
			public void beginArray(JsonContext context) {
				result += "[";
			}

			@Override
			public void endArray(JsonContext context) {
				result += "]";
			}

			@Override
			public void property(JsonContext context, String propertyName) {
				result += "(p="+propertyName+")";
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
			}});
	}
	
	@After
	public void after() {
		assertThat(exceptionCaught, is(true));
	}
	
	@Test
	public void testIllegalPropertyNameObject() throws Exception {
		String text = "{{\"a\":false}:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testIllegalPropertyNameArray() throws Exception {
		String text = "{[1]:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testIllegalPropertyNameNumber() throws Exception {
		String text = "{1:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testIllegalPropertyNameTrue() throws Exception {
		String text = "{true:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testIllegalPropertyNameFalse() throws Exception {
		String text = "{false:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testIllegalPropertyNameNull() throws Exception {
		String text = "{null:true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testWrongObjectBrackets() throws Exception {
		String text = "[\"a\":true]";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testWrongArrayBrackets() throws Exception {
		String text = "{\"a\", true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testMissingEndAray() throws Exception {
		String text = "{\"a\": [true}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testMissingCommaBetweenProperties() throws Exception {
		String text = "{\"a\": true \"b\":false}";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
	@Test
	public void testMissingCommaInArray() throws Exception {
		String text = "[\"a\", true \"b\", false]";
		try {
			json.parse(text);
		} catch(JsonIllegalSyntaxException e) {
			exceptionCaught = true;
		}	
	}
	
//	@Test
//	public void testMissingLastValue() throws Exception {
//		Diesen Fehler müsste man im Parser extra detektieren!
//		Der Parser verhält sich in diesem Fall korrekt und beendet
//      Das Array wie erwartet.
//		Ein möglicher Fehler bei der Erzeugung de JSON würde nicht
//      erkannt. Dies ist jedoch sehr unwahrscheinlich (manuell editiert).
//
//		String text = "[true,]";
//		try {
//			json.parse(text);
//		} catch(JsonIllegalSyntaxException e) {
//			exceptionCaught = true;
//		}	
//	}
}
