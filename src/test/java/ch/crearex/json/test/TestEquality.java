package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.CrearexJson;

public class TestEquality {

	CrearexJson json = null;
	
	@Before
	public void before() {
		json = new CrearexJson();
	}
	
	@Test
	public void testSimpleDataTypes() {
		String text1 = "{\"d\": 12.3, \"s\": \"abc\", \"i\":3, \"b\": true, \"n\": null }";
		JsonDocument doc1 = json.parse(text1);
		
		String text2 = "{\"s\": \"abc\", \"d\": 12.3, \"i\":3, \"b\": true, \"n\": null }";
		JsonDocument doc2 = json.parse(text2);
		
		String text3 = "{\"s\": \"xyz\", \"d\": 12.3, \"i\":3, \"b\": true, \"n\": null }";
		JsonDocument doc3 = json.parse(text3);
		
		assertThat(doc1, is(doc2));
		assertThat(doc1, not(is(doc3)));
	}
	
	@Test
	public void testJsonValue() {
		JsonObject obj1 = new JsonObject();
		obj1.add("n", (String)null);
		obj1.add("b", true);
		obj1.add("i", 3);
		obj1.add("s", "abc");
		obj1.add("d", 12.3);
		
		JsonObject obj2 = new JsonObject();
		obj2.add("s", "abc");
		obj2.add("n", (String)null);
		obj2.add("b", true);
		obj2.add("d", 12.3);
		obj2.add("i", 3);
		
		assertThat(obj1, is(obj2));
	}
	
	@Test
	public void testJsonValueMissing() {
		JsonObject obj1 = new JsonObject();
		obj1.add("n", (String)null);
		obj1.add("b", true);
		obj1.add("s", "abc");
		obj1.add("d", 12.3);
		
		JsonObject obj2 = new JsonObject();
		obj2.add("s", "abc");
		obj2.add("n", (String)null);
		obj2.add("b", true);
		
		assertThat(obj1, not(is(obj2)));
		assertThat(obj2, not(is(obj1)));
	}
	
	@Test
	public void testMixedJsonValue() {
		String text1 = "{\"d\": 12.3, \"i\":3, \"s\": \"abc\", \"b\": true, \"n\": null }";
		JsonDocument doc1 = json.parse(text1);
		
		JsonObject obj = new JsonObject();
		obj.add("n", (String)null);
		obj.add("b", true);
		obj.add("s", "abc");
		obj.add("d", 12.3);
		obj.add("i", 3);
		
		assertThat(obj, is(doc1.getRootObject()));
	}
	
	@Test
	public void testSimpleDataTypesArray() {
		String text1 = "[12.3, \"abc\", 3, true, null ]";
		JsonDocument doc1 = json.parse(text1);
		
		String text2 = "[12.3, \"abc\", 3, true, null ]";
		JsonDocument doc2 = json.parse(text2);
		
		String text3 = "[12.3, \"abc\", 3, null, true ]";
		JsonDocument doc3 = json.parse(text3);
		
		assertThat(doc1, is(doc2));
		assertThat(doc1, not(is(doc3)));
	}
	
	@Test
	public void testSimpleDataTypesArrayMixed() {
		String text1 = "[12.3, \"abc\", 3, true, null ]";
		JsonDocument doc1 = json.parse(text1);
		
		String text2 = "[12.3, \"abc\" ]";
		JsonDocument doc2 = json.parse(text2);
		JsonArray arr = doc2.getRootArray();
		arr.add(3);
		arr.add(true);
		arr.addNull();
		
		String text3 = "[12.3, \"abc\", 3, null, true ]";
		JsonDocument doc3 = json.parse(text3);
		
		assertThat(doc1, is(doc2));
		assertThat(doc1, not(is(doc3)));
	}
	
	@Test
	public void testSimpleDataTypesArrayMissingEntry() {
		String text1 = "[12.3, \"abc\", 3, true, null ]";
		JsonDocument doc1 = json.parse(text1);
		
		String text2 = "[12.3, \"abc\", 3, true ]";
		JsonDocument doc2 = json.parse(text2);

		assertThat(doc1, not(is(doc2)));
		assertThat(doc2, not(is(doc1)));
	}
	
	@Test
	public void testSimpleDataTypesArrayJsonValue() {
		JsonArray obj1 = new JsonArray();
		obj1.add((String)null);
		obj1.add(true);
		obj1.add(3);
		obj1.add("abc");
		obj1.add(12.3);
		
		JsonArray obj2 = new JsonArray();
		obj2.add((String)null);
		obj2.add(true);
		obj2.add(3);
		obj2.add("abc");
		obj2.add(12.3);

		assertThat(obj1, is(obj2));
	}
	
	@Test
	public void testSimpleDataTypesArrayJsonValueWrongOrder() {
		JsonArray obj1 = new JsonArray();
		obj1.add((String)null);
		obj1.add(true);
		obj1.add(3);
		obj1.add("abc");
		obj1.add(12.3);
		
		JsonArray obj2 = new JsonArray();
		obj2.add("abc");
		obj2.add((String)null);
		obj2.add(true);
		obj2.add(12.3);
		obj2.add(3);

		assertThat(obj1, not(is(obj2)));
		assertThat(obj2, not(is(obj1)));
	}
	
	@Test
	public void testComplex() {
		String text1 = "{" +
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
		String text2 = "{" +
				"\"name\":\"Niedermann\"," +
				"\"vorname\":\"Markus\"," +
				"\"aktiv\":true," +
				"\"adresse\":{" +
				    "\"ort\":\"St. Gallen\","  +
				    "\"strasse\":\"Musterweg 1\"," +
					"\"plz\":9016" + 
				"}," + 
				"\"books\":[" +
				"\"Domain Driven Design\"," +
				"\"Design Patterns\"," +
				"{\"name\":\"Tiptopf\",\"ISBN\":\"123456789\"}"+"]" +
	 	    "}";
		String text3 = "{" +
				"\"name\":\"Niedermann\"," +
				"\"vorname\":\"Markus\"," +
				"\"aktiv\":true," +
				"\"adresse\":{" +
				    "\"ort\":\"Zürich\","  +
				    "\"strasse\":\"Musterweg 1\"," +
					"\"plz\":9016" + 
				"}," + 
				"\"books\":[" +
				"\"Domain Driven Design\"," +
				"\"Design Patterns\"," +
				"{\"name\":\"Tiptopf\",\"ISBN\":\"123456789\"}"+"]" +
	 	    "}";
		String text4 = "{" +
				"\"name\":\"Niedermann\"," +
				"\"vorname\":\"Markus\"," +
				"\"adresse\":{" +
				    "\"strasse\":\"Musterweg 1\"," +
					"\"ort\":\"St. Gallen\","  +
					"\"plz\":9016" + 
				"}," + 
				"\"aktiv\":true," +
				"\"books\":[" +
				"\"Design Patterns\"," +
				"\"Domain Driven Design\"," +
				"{\"name\":\"Tiptopf\",\"ISBN\":\"123456789\"}"+"]" +
	 	    "}";
		JsonDocument doc1 = json.parse(text1);
		JsonDocument doc1a = json.parse(text1);
		JsonDocument doc2 = json.parse(text2);
		JsonDocument doc3 = json.parse(text3);
		JsonDocument doc4 = json.parse(text4);
		
		
		assertThat(doc1, is(doc1a));
		assertThat(doc1a, is(doc1));
		
		assertThat(doc1, is(doc2));
		assertThat(doc2, is(doc1));
		
		assertThat(doc1, not(is(doc3)));
		assertThat(doc3, not(is(doc1)));
		
		assertThat(doc1, not(is(doc4)));
		assertThat(doc4, not(is(doc1)));
		
		doc1.getRootObject().add("test", 123);
		doc2.getRootObject().add("test", 123);
		assertThat(doc1, is(doc2));
		assertThat(doc2, is(doc1));
	}
}
