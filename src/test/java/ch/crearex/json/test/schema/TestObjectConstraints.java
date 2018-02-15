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

public class TestObjectConstraints {

	@Test
	public void textMaxPropertiesOk() throws Exception {
		String text = "{\"a\":\"a\", \"b\":\"b\", \"c\":\"c\", \"4\":\"d\", \"5\":\"e\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void textMaxPropetiesWithObjectOk() throws Exception {
		String text = "{\"a\":\"a\", \"b\":\"b\", \"c\":\"c\", \"4\":\"d\", \"5\":{\"x\":1}}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void textMaxPropetiesFailed() throws Exception {
		String text = "{\"a\":\"a\", \"b\":\"b\", \"c\":\"c\", \"4\":\"d\", \"5\":\"e\", \"6\":\"f\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}
	
	@Test
	public void textMaxPropetiesWithObjectFailed() throws Exception {
		String text = "{\"a\":\"a\", \"b\":\"b\", \"c\":\"c\", \"4\":\"d\", \"5\":\"e\", \"6\":{\"x\":1}}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}
	
	@Test
	public void textMinPropertiesOk() throws Exception {
		String text = "{\"a\":\"a\", \"b\":\"b\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));
	}
	
	@Test
	public void textMinPropetiesFailed() throws Exception {
		String text = "{\"a\":\"a\"}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/object-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));
	}
}
