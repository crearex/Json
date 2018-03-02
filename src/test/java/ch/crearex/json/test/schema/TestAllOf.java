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

public class TestAllOf {

	@Test
	public void testAllOfOk() throws Exception {
		String text = "{"+
				"\"street_address\": \"1600 Pennsylvania Avenue NW\","+
				"\"city\": \"Washington\","+
				"\"state\": \"DC\","+
				"\"type\": \"business\""+
				"}";
		
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/allof-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void testAllOfFailed1() throws Exception {
		String text = "{"+
				"\"street_address\": \"1600 Pennsylvania Avenue NW\","+
				"\"city\": \"Washington\","+
				"\"state\": \"DC\","+
				"\"type\": \"ILEGAL ENUM VALUE\""+
				"}";
		
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/allof-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}
}
