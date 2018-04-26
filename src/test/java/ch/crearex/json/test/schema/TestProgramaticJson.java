package ch.crearex.json.test.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.SchemaValidationStatus;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestProgramaticJson {
	
	@Test
	public void testSimpleJson() {
		JsonObject person = new JsonObject();
		person.add("name", "Hans");
		person.add("age", 34);
		
		JsonObject address = new JsonObject();
		address.add("city", "Zürich");
		address.add("code", 8000);
		
		person.add("address", address);
		
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestProgramaticJson.class.getResource("/json-schema.json"));
		JsonDocument doc = new JsonDocument(person);
		doc.validate(schema);
		System.out.println(doc.getValidationErrorsSummary());
		assertThat(doc.getValidationStatus(), is(SchemaValidationStatus.VALID));
	}
}
