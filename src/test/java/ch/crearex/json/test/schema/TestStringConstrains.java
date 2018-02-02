package ch.crearex.json.test.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.TestUtil;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.SchemaValidationStatus;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestStringConstrains {
	@Test 
	public void testRegexOk() throws Exception {
		String text = "{\"text\":\"a1e\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/string-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testRegexFailed() throws Exception {
		String text = "{\"text\":\"9x9\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/string-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
}
