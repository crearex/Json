package ch.crearex.json.test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.Json;
import ch.crearex.json.JsonDomCallback;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSchemaCallback;
import ch.crearex.json.TestUtil;
import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.SchemaValidationStatus;
import ch.crearex.json.impl.CrearexJson;

public class TestJson {
	
	private ArrayList<JsonDocument> documents = new ArrayList<JsonDocument>();
	private ArrayList<String> schemaMessages = new ArrayList<String>();
	
	@Before
	public void clear() {
		documents.clear();
		schemaMessages.clear();
	}
	
	@Test
	public void testParseString() throws Exception {
		String text = "{\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		Json json = new CrearexJson();
		JsonDocument doc = json.parse(text);
		assertThat(doc.getValidationStatus(), is(SchemaValidationStatus.NOT_VALIDATED));
		assertThat(doc.getRootObject().getString("name"), is("Felix"));
		clear();
		
		json.setSchema(TestUtil.readResource("/json-schema.json")).setSchemaCallback( new JsonSchemaCallback() {
			@Override
			public void schemaViolation(JsonPath path, String message) {
				schemaMessages.add(path.toString() + ": " + message);
			}
		});
		doc = json.parse(text);
		assertThat(doc.getRootObject().getString("name"), is("Felix"));
		clear();
		
		String text2 = "{\"name\":\"Felix\",\"age\":\"25\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}";
		doc = json.parse(text2);
		assertThat(schemaMessages.size(), is(1));
		clear();
		
		String text3 = "{\"name\":\"Felix\",\"age\":25,\"address\":{\"city\":\"Gränchen\",\"code\":1234}}" +
				       "{\"name\":\"Hans\",\"age\":34,\"address\":{\"city\":\"Mühleberg\",\"code\":5678}}";;
		
	    json.setDomCallback(new JsonDomCallback() {
			@Override
			public void onDocument(JsonDocument document) {
				documents.add(document);
			}});
		json.parse(text3);		       
		assertThat(documents.size(), is(2));
		assertThat(documents.get(0).getRootObject().getString("name"), is("Felix"));
		assertThat(documents.get(1).getRootObject().getString("name"), is("Hans"));
		assertThat(schemaMessages.size(), is(0));
		clear();
		
		String text4 = "{\"name\":\"Felix\",\"age\":\"25\",\"address\":{\"city\":\"Gränchen\",\"code\":1234}}" +
			       "{\"name\":\"Hans\",\"age\":34,\"address\":{\"city\":\"Mühleberg\",\"code\":5678}}";;

       json.parse(text4);		       
		assertThat(documents.size(), is(2));
		assertThat(documents.get(0).getValidationStatus(), is(SchemaValidationStatus.INVALID));
		assertThat(documents.get(0).getRootObject().getString("name"), is("Felix"));	 
		assertThat(documents.get(1).getValidationStatus(), is(SchemaValidationStatus.VALID));
		assertThat(documents.get(1).getRootObject().getString("name"), is("Hans"));	 
		assertThat(schemaMessages.size(), is(1));
		
		clear();
		try {
			json.setSchemaCallback(null);
			json.parse(text4);
			assertTrue(false);
		} catch(Exception e) {
			// ok - Default behaviour without JsonSchemaCallback: Validation Exception catched!
		}
		
	}
}
