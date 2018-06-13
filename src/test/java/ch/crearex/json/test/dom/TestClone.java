package ch.crearex.json.test.dom;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.impl.CrearexJson;

public class TestClone {

private CrearexJson json = null;
	
	@Before
	public void before() {
		json = new CrearexJson();
	}
	
	@Test
	public void testClone() {
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
				JsonDocument clone = doc.clone();
				
				assertThat(doc, is(clone));
				
				
	}
}
