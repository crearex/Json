package ch.crearex.json.test.dom;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.dom.JsonAccessException;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonValue;
import ch.crearex.json.impl.CrearexJson;

public class TestJsonDomValue {
	
	@Test
	public void testNumber() {
		double value = 1.23456789;
		JsonValue domValue = new JsonValue(value);
		assertThat(domValue.asString(), is("1.23456789"));
		
		value = 0.23;
		domValue = new JsonValue(value);
		assertThat(domValue.asString(), is("0.23"));
		
		value = 56.234;
		domValue = new JsonValue(value);
		assertThat(domValue.asString(), is("56.234"));
		
		value = 1;
		domValue = new JsonValue(value);
		assertThat(domValue.asString(), is("1.0"));
	}
	
	@Test
	public void readNotExistingShortValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			short val = doc.getRootObject().getShort("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		short val = doc.getRootObject().getShort("val", (short)3);
		assertThat(val, is((short)3));
	}
	
	@Test
	public void readShortValue() {
		String text = "{\"val\":123}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			short val = doc.getRootObject().getShort("val");
			assertThat(val, is((short)123));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readShortNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			short val = doc.getRootObject().getShort("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		short val = doc.getRootObject().getShort("val", (short)3);
		assertThat(val, is((short)3));
	}
	
	@Test
	public void readNotExistingIntegerValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			int val = doc.getRootObject().getInteger("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		int val = doc.getRootObject().getInteger("val", 3);
		assertThat(val, is(3));
	}
	
	@Test
	public void readIntegerValue() {
		String text = "{\"val\":12345}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			int val = doc.getRootObject().getInteger("val");
			assertThat(val, is(12345));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readIntegerNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			int val = doc.getRootObject().getInteger("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		int val = doc.getRootObject().getInteger("val", 3);
		assertThat(val, is(3));
	}
	
	public void readNotExistingLongValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			long val = doc.getRootObject().getLong("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		long val = doc.getRootObject().getLong("val", 3l);
		assertThat(val, is(3l));
	}
	
	@Test
	public void readBooleanValue() {
		String text = "{\"val\":true,\"val2\":false}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			boolean val = doc.getRootObject().getBoolean("val");
			assertThat(val, is(true));
			boolean val2 = doc.getRootObject().getBoolean("val2");
			assertThat(val2, is(false));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(true));
			assertThat(doc.getRootObject().isNumber("val"), is(false));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readBooleanNullValue() {
		String text = "{\"val\":null,\"val2\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			int val = doc.getRootObject().getInteger("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		try {
			int val2 = doc.getRootObject().getInteger("val2");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		boolean val = doc.getRootObject().getBoolean("val", true);
		assertThat(val, is(true));
		boolean val2 = doc.getRootObject().getBoolean("val", false);
		assertThat(val2, is(false));
	}
	
	public void readNotExistingBooleanValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			boolean val = doc.getRootObject().getBoolean("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		boolean val = doc.getRootObject().getBoolean("val", true);
		assertThat(val, is(true));
		boolean val2 = doc.getRootObject().getBoolean("val2", false);
		assertThat(val, is(false));
	}
	
	@Test
	public void readLongValue() {
		String text = "{\"val\":12345}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			long val = doc.getRootObject().getLong("val");
			assertThat(val, is(12345l));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readLongNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			long val = doc.getRootObject().getLong("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		long val = doc.getRootObject().getLong("val", 3l);
		assertThat(val, is(3l));
	}
	
	public void readNotExistingFloatValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			float val = doc.getRootObject().getFloat("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		float val = doc.getRootObject().getFloat("val", 3.2f);
		assertThat(val, is(3.2f));
	}
	
	@Test
	public void readFloatValue() {
		String text = "{\"val\":1234.5678}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			float val = doc.getRootObject().getFloat("val");
			assertThat(val, is(1234.5678f));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readFloatNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			float val = doc.getRootObject().getFloat("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		float val = doc.getRootObject().getFloat("val", 3.2f);
		assertThat(val, is(3.2f));
	}
	
	public void readNotExistingDoubleValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			double val = doc.getRootObject().getDouble("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		double val = doc.getRootObject().getDouble("val", 3.1);
		assertThat(val, is(3.1));
	}
	
	@Test
	public void readDoubleValue() {
		String text = "{\"val\":1234.56789}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			double val = doc.getRootObject().getDouble("val");
			assertThat(val, is(1234.56789));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readDoubleNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			double val = doc.getRootObject().getDouble("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		double val = doc.getRootObject().getDouble("val", 3.1);
		assertThat(val, is(3.1));
	}
	
	public void readNotExistingStringValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			String val = doc.getRootObject().getString("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		String val = doc.getRootObject().getString("val", "Hello");
		assertThat(val, is("Hello"));
	}
	
	@Test
	public void readStringValue() {
		String text = "{\"val\":\"Hello\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			String val = doc.getRootObject().getString("val");
			assertThat(val, is("Hello"));
			
			assertThat(doc.getRootObject().isString("val"), is(true));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(false));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readStringNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			String val = doc.getRootObject().getString("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		String val = doc.getRootObject().getString("val", "Hello");
		assertThat(val, is("Hello"));
	}
	
	public void readNotExistingBigIntegerValue() {
		String text = "{}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {			
			BigInteger val = doc.getRootObject().getBigInteger("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		BigInteger val = doc.getRootObject().getBigInteger("val", new BigInteger("123456789"));
		assertThat(val, is(new BigInteger("123456789")));
	}
	
	@Test
	public void readBigIntegerValue() {
		String text = "{\"val\":123456789}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			BigInteger val = doc.getRootObject().getBigInteger("val");
			assertThat(val, is(new BigInteger("123456789")));
			
			assertThat(doc.getRootObject().isString("val"), is(false));
			assertThat(doc.getRootObject().isBoolean("val"), is(false));
			assertThat(doc.getRootObject().isNumber("val"), is(true));
			assertThat(doc.getRootObject().isNull("val"), is(false));
		} catch(Exception e) {
			assertTrue("Unexpected Exception", false);
		}
	}
	
	@Test
	public void readBigIntegerNullValue() {
		String text = "{\"val\":null}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		try {
			BigInteger val = doc.getRootObject().getBigInteger("val");
			assertTrue("Missing Exception", false);
		} catch(JsonAccessException e) {
		} catch(Exception e) {
			assertTrue("Wrong Exception", false);
		}
		
		BigInteger val = doc.getRootObject().getBigInteger("val", new BigInteger("123456789"));
		assertThat(val, is(new BigInteger("123456789")));
	}
}
