package ch.crearex.json.test.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonParserFactory;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSchema;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.TestUtil;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.SchemaValidationStatus;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;
import ch.crearex.json.schema.JsonSchemaValidationException;

public class TestReferences {

	@Test
	public void testSingleIdReference() throws Exception {
		String textOk = "{\"name\":\"Hans\", \"age\": 33}";
		Json json = new CrearexJson();
		URL schemaUrl = TestUtil.getResourceUrl("/ref-schema.json");
		json.setSchema(schemaUrl);
		JsonDocument doc = json.parse(textOk);
		int age = doc.getRootObject().getInteger("age", -1);
		assertThat(age, is(33));
		
		String textFailure = "{\"name\":\"Hans\", \"age\": \"33\"}";
		try {
			doc = json.parse(textFailure);
			assertTrue(false);
		} catch(JsonSchemaValidationException e) {
			System.out.println(e.getMessage());
			// ok
		}	
		
		json.clear();
		textFailure = "{\"name\":\"Hans\", \"age\": 33}";
		try {
			doc = json.parse(textFailure);	
		} catch(JsonSchemaValidationException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
		
		json.clear();
		textFailure = "{\"age\": 33}";
		try {
			doc = json.parse(textFailure);
			assertTrue(false);
		} catch(JsonSchemaValidationException e) {
			System.out.println(e.getMessage());
			// ok
		}
		
	}
	
	@Test
	public void testSingleIdReferenceMissingProperty() throws Exception {

		String textFailure = "{\"age\": 33}";
		try {
			Json json = new CrearexJson();
			URL schemaUrl = TestUtil.getResourceUrl("/ref-schema.json");
			json.setSchema(schemaUrl);
			JsonDocument doc = json.parse(textFailure);
			assertTrue(false);
		} catch(JsonSchemaValidationException e) {
			System.out.println(e.getMessage());
			// ok
		}		
	}
}
