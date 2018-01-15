package ch.crearex.json.test.dom;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.JsonValue;

public class TestProgrammaticallyCreatedDom {

	@Test
	public void testCreateDomDocument() {
		JsonDocument doc = JsonDocument.createObjectDocument();
		doc.getRootObject().add("name", "Markus");
		String result = doc.toString();
		assertThat(result, is("{\"name\":\"Markus\"}"));
	}
	
	@Test
	public void testCreateObject() {
		JsonObject obj = new JsonObject();
		obj.add("name", new JsonValue("Markus"));
		String result = obj.toString();
		assertThat(result, is("{\"name\":\"Markus\"}"));
	}
	
	@Test
	public void testCreateComplexObject() {
		JsonObject obj = new JsonObject();
		obj.add("name", "Markus");
		obj.add("x", new JsonObject().add("a", true));
		obj.add("y", "test");
		obj.add("z", new JsonArray().add(true).add(false).addNull());
		String result = obj.toString();
		assertThat(result, is("{\"name\":\"Markus\",\"x\":{\"a\":true},\"y\":\"test\",\"z\":[true,false,null]}"));
		
		JsonObject x = obj.getObject("x");
		assertThat(x.getParent(), is(obj));
		
		x = (JsonObject)obj.remove("x");
		assertThat(x.getParent(), is((JsonObject)null));
		
		JsonArray z = obj.getArray("z");
		assertThat(z.getParent(), is(obj));
		
		z = (JsonArray)obj.remove("z");
		assertThat(z.getParent(), is((JsonArray)null));
	}
}
