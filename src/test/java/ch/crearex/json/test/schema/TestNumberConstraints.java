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

public class TestNumberConstraints {
	@Test 
	public void testIntegerMaximumOk() throws Exception {
		String text = "{\"minMaxInteger\":100}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testIntegerMaximumFailed() throws Exception {
		String text = "{\"minMaxInteger\":101}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testIntegerMinimumOk() throws Exception {
		String text = "{\"minMaxInteger\":0}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testIntegerMinimumFailed() throws Exception {
		String text = "{\"minMaxInteger\":-1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testDoubleMaximumOk() throws Exception {
		String text = "{\"minMaxDouble\":100.1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testDoubleMaximumFailed() throws Exception {
		String text = "{\"minMaxDouble\":100.101}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testDoubleMinimumOk() throws Exception {
		String text = "{\"minMaxDouble\":0.1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testDoubleMinimumFailed() throws Exception {
		String text = "{\"minMaxDouble\":0.001}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	// -----------------------
	
	@Test 
	public void testExclusiveIntegerMaximumOk() throws Exception {
		String text = "{\"exclusiveMinMaxInteger\":99}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testExclusiveIntegerMaximumFailed() throws Exception {
		String text = "{\"exclusiveMinMaxInteger\":100}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testExclusiveIntegerMinimumOk() throws Exception {
		String text = "{\"exclusiveMinMaxInteger\":1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testExclusiveIntegerMinimumFailed() throws Exception {
		String text = "{\"exclusiveMinMaxInteger\":0}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testExclusiveDoubleMaximumOk() throws Exception {
		String text = "{\"exclusiveMinMaxDouble\":100.099}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testExclusiveDoubleMaximumFailed() throws Exception {
		String text = "{\"exclusiveMinMaxDouble\":100.1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testExclusiveDoubleMinimumOk() throws Exception {
		String text = "{\"exclusiveMinMaxDouble\":0.1001}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testExclusiveDoubleMinimumFailed() throws Exception {
		String text = "{\"exclusiveMinMaxDouble\":0.1}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	// -----------------------
	
	@Test 
	public void testMultipleOfIntegerOk() throws Exception {
		String text = "{\"multipleOfInteger\":6}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testMultipleOfIntegerFailed() throws Exception {
		String text = "{\"multipleOfInteger\":7}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
	
	@Test 
	public void testMultipleOfDoubleOk() throws Exception {
		String text = "{\"multipleOfDouble\":10.0}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.VALID));			
	}
	
	@Test 
	public void testMultipleOfDoubleFailed() throws Exception {
		String text = "{\"multipleOfDouble\":10.001}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		JsonSchema schema = new CrearexJsonParserFactory().createJsonSchema(TestUtil.readResource("/number-constraint-schema.json"));
		assertThat(doc.validate(schema), is(SchemaValidationStatus.FAILED));			
	}
}
