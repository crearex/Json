/**
 * API for parsing JSON Documents.<br>
 * <br>
 * <b>JSON Callback</b><br>
 * <br>
 * The fundamental Crearex JSON API is based on a simple
 * {@link ch.crearex.json.JsonParser JSON Parser} which notifies tokens by a
 * {@link ch.crearex.json.JsonCallback callback}.<br>
 * You can you this simple API to implement more sophisticated libraries like
 * the {@link ch.crearex.json.dom} API.<br>
 * <br>
 * 
 * <pre>
 * JsonParserFactory factory = new CrearexJsonParserFactory();
 * JsonCallback callback = new JsonCallback() {
 * 
 * 	&#64;Override
 * 	public void beginDocument(JsonContext context) {
 * 		System.out.println("BEGIN DOCUMENT");
 * 	}
 * 
 * 	&#64;Override
 * 	public void endDocument(JsonContext context) {
 * 		System.out.println("END DOCUMENT");
 * 	}
 * 
 * 	&#64;Override
 * 	public void beginObject(JsonContext context) {
 * 		System.out.println("BEGIN OBJECT");
 * 	}
 * 
 * 	&#64;Override
 * 	public void endObject(JsonContext context) {
 * 		System.out.println("END OBJECT");
 * 	}
 * 
 * 	&#64;Override
 * 	public void beginArray(JsonContext context) {
 * 		System.out.println("BEGIN ARRAY");
 * 	}
 * 
 * 	&#64;Override
 * 	public void endArray(JsonContext context) {
 * 		System.out.println("END ARRAY");
 * 	}
 * 
 * 	&#64;Override
 * 	public void property(JsonContext context, String propertyName) {
 * 		System.out.println("PROPERTY: " + propertyName);
 * 	}
 * 
 * 	&#64;Override
 * 	public void simpleValue(JsonContext context, JsonSimpleValue value) {
 * 		System.out.println("VALUE: " + value);
 * 	}
 * 
 * 	&#64;Override
 * 	public void comma(JsonContext context) {
 * 		System.out.println("COMMA");
 * 	}
 * };
 * 
 * JsonParser parser = factory.createJsonParser(callback);
 * 
 * String text = "{\"greeting\": \"Hello World!\"}";
 * parser.parse(text);
 * 
 * // Output:
 * // -------------------------
 * // BEGIN DOCUMENT
 * // BEGIN OBJECT
 * // PROPERTY: greeting
 * // VALUE: Hello World!
 * // END OBJECT
 * // END DOCUMENT
 * </pre>
 * 
 * <b>Simple DOM Access</b>
 * <br>
 * <pre>
 * String text = "{\"greeting\": \"Hello World!\"}";
 * Json json = new CrearexJson();
 * JsonDocument document = json.parse(text);
 * String greeting = document.getRootObject().getString("greeting");
 * 
 * // Output:
 * // -------------------------
 * // greeting = "Hello World!"
 * </pre>
 * 
 * <b>Customized DOM Access</b><br>
 * <br>
 * It's possible to extend the DOM Builder by providing
 * a {@link ch.crearex.json.JsonDomCallback DOM Callback}, a {@link ch.crearex.json.JsonContextBase Context}
 * or a {@link ch.crearex.json.JsonValueFactory Value Factory}:<br>
 * <pre>
 * String text = "{\"greeting\": \"Hello World!\"}";
 * 
 * JsonParserFactory factory = new CrearexJsonParserFactory();
 * JsonDomCallback domCallback = new JsonDomCallback() {
 * 	&#64;Override
 * 	public void onDocument(JsonDocument document) {
 * 		System.out.println("NEW DOCUMENT: " + document);
 * 	}
 * };
 * JsonDomBuilder domBuilder = new JsonDomBuilder(domCallback) {
 * 	// Overwrite createContext() to customize the context
 * 	public JsonContextBase createContext(JsonCallback callback) {
 * 		return new JsonDomContext(callback);
 * 	}
 * 
 * 	public JsonValueFactory createJsonValueFactory(JsonContextBase context) {
 * 		// You'll get your customized context
 * 		JsonDomContext domContext = (JsonDomContext) context;
 * 		// Customize the value Factory e.g.
 * 		return new JsonDomValueFactory(domContext) {
 * 			&#64;Override
 * 			public JsonSimpleValue createStringValue(String value) {
 * 				return new JsonString(getContext(), value) {
 * 					&#64;Override
 * 					public String asString() {
 * 						String value = super.asString();
 * 						return ":-) " + value;
 * 					}
 * 				};
 * 			}
 * 		};
 * 	}
 * };
 * 
 * JsonParser parser = factory.createJsonParser(domBuilder);
 * parser.parse(text);
 * 
 * // Output:
 * // -------------------------
 * // NEW DOCUMENT: {"greeting":":-) Hello World!"}
 * </pre>
 * 
 * 
 */
package ch.crearex.json;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonDomBuilder;
import ch.crearex.json.dom.JsonDomContext;
import ch.crearex.json.dom.JsonDomValueContextFactory;
import ch.crearex.json.dom.JsonString;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.impl.CrearexJsonParserFactory;
