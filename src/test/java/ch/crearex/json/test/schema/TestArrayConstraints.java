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

public class TestArrayConstraints {
	
	@Test
	public void textMaxItemsOk() throws Exception {
		String text = "{\"numbers\":[1,2,3,4,5]}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/array-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void textMaxItemsFalied() throws Exception {
		String text = "{\"numbers\":[1,2,3,4,5,6]}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/array-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}
	
	@Test
	public void textMinItemsOk() throws Exception {
		String text = "{\"numbers\":[1,2]}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/array-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void textMinItemsFalied() throws Exception {
		String text = "{\"numbers\":[1]}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/array-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}

}
