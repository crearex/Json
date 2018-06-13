package ch.crearex.json.test.dom;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonPath;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.impl.CrearexJson;

public class TestJsonPathQuery {
	
	private CrearexJson json = null;
	
	@Before
	public void before() {
		json = new CrearexJson();
	}
	
	@Test
	public void testQueryDocument() {
		String text =
		"[" +		
			"{" +
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
	 	    "}," +
	 	   "{" +
				"\"name\":\"Muster\"," +
				"\"vorname\":\"Felix\"," +
				"\"adresse\":{" +
				    "\"strasse\":\"Säntisstrasse 1\"," +
					"\"ort\":\"Herisau\","  +
					"\"plz\":1234" + 
				"}," + 
				"\"aktiv\":true," +
				"\"books\":[" +
				"\"Harry Potter\"," +
				"\"Atlas\"," +
				"{\"name\":\"Clean Code\",\"ISBN\":\"3456\"}"+"]" +
		    "}," +
		    "{" +
				"\"name\":\"Müller\"," +
				"\"vorname\":\"Heinz\"," +
				"\"adresse\":{" +
				    "\"strasse\":\"Florastrasse 1\"," +
					"\"ort\":\"Bern\","  +
					"\"plz\":7654" + 
				"}," + 
				"\"aktiv\":true," +
				"\"books\":[" +
				"\"Telefonbuch\"," +
				"]" +
	 	    "}" +
		"]";
		
		JsonDocument doc = json.parse(text);
		List<JsonElement> result = doc.query(new JsonPath("$.*.name"));
		result.toArray();
		assertThat(result.size(), is(3));
		assertThat(result.get(0).toString(), is("Niedermann"));
		assertThat(result.get(1).toString(), is("Muster"));
		assertThat(result.get(2).toString(), is("Müller"));
			
	}
}
