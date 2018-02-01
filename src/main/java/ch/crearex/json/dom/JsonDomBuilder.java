package ch.crearex.json.dom;

import java.util.Stack;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonDomCallback;
import ch.crearex.json.JsonException;
import ch.crearex.json.JsonIllegalSyntaxException;
import ch.crearex.json.JsonValueFactory;
import ch.crearex.json.JsonValueFactoryProviderCallback;

/**
 * The {@link JsonCallback} which builds the JSON DOM Tree.
 * The resulting {@link JsonDocument} can be retrieved by @link {@link JsonDomBuilder#getDocument()}
 * or by notification if a {@link JsonDomCallback} is used (see {@link JsonDomBuilder#JsonDomBuilder(JsonDomCallback)}).
 * 
 * @author Markus Niedermann
 *
 */
public class JsonDomBuilder implements JsonValueFactoryProviderCallback {

	private final Stack<JsonContainer> activeContainer = new Stack<JsonContainer>();
	private final JsonDomCallback callback;
	private JsonDocument document;
	private boolean firstContainer = true;
	private String nextPropertName;
	private JsonContextBase context = null;
	
	public JsonDomBuilder() {
		this(null);
	}
	
	public JsonDomBuilder(JsonDomCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public JsonContextBase createContext(JsonCallback callback) {
		return new JsonDomContext(callback);
	}
	
	@Override
	public JsonValueFactory createJsonValueFactory(JsonContextBase context) {
		JsonDomContext domContext = (JsonDomContext)context;
		return new JsonDomValueFactory(domContext);
	}

	@Override
	public void beginDocument(JsonContext context) {
		reset();
		this.context = (JsonContextBase)context;
	}

	@Override
	public void endDocument(JsonContext context) {
		notifyEndDocument();
	}

	@Override
	public void beginObject(JsonContext context) {
		JsonObject obj = createObject();
		if(firstContainer) {
			firstContainer = false;
			document.setRoot(obj);
		} else {
			doAddContainer(obj);
		}
		activeContainer.push(obj);
	}

	@Override
	public void endObject(JsonContext context) {
		activeContainer.pop();
	}

	@Override
	public void beginArray(JsonContext context) {
		JsonArray obj = createArray();
		if(firstContainer) {
			firstContainer = false;
			document.setRoot(obj);
		} else {
			doAddContainer(obj);
		}	
		activeContainer.push(obj);
	}


	private void doAddContainer(JsonContainer obj) {
		JsonContainer container = activeContainer.peek();
		if(container instanceof JsonObject) {
			((JsonObject)container).add(nextPropertName, obj);
			nextPropertName = null;
		} else {
			// container is for sure a JsonArray...
			((JsonArray)container).add(obj);
		}
	}

	@Override
	public void endArray(JsonContext context) {
		activeContainer.pop();
	}

	@Override
	public void property(JsonContext context, String propertyName) {
		this.nextPropertName = propertyName;		
	}

	@Override
	public void simpleValue(JsonContext context, JsonSimpleValue value) {
		JsonContainer container = activeContainer.peek();
		if(container instanceof JsonObject) {
			((JsonObject)container).add(nextPropertName, (JsonElement)value);
			nextPropertName = null;
		} else {
			((JsonArray)container).add((JsonElement)value);
		}
	}
	
	@Override
	public void comma(JsonContext context) {
		// empty ok
	}
	
	protected JsonObject createObject() {
		JsonContainer parent = getActualContainer();
		return new JsonObject(parent);
	}

	protected JsonArray createArray() {
		JsonContainer parent = getActualContainer();
		return new JsonArray(parent);
	}
	
	private JsonContainer getActualContainer() {
		JsonContainer parent = null;
		if(!activeContainer.isEmpty()) {
			parent = activeContainer.peek();
		}
		return parent;
	}

	JsonContainer getActiveContainer() {
		return activeContainer.peek();
	}
	
	public JsonDomBuilder reset() {
		document = JsonDocument.createEmptyDocument();
		activeContainer.clear();
		firstContainer = true;
		nextPropertName = null;
		context = null;
		return this;
	}
	
	public JsonContext getContext() {
		return context;
	}
	
	/**
	 * @throws JsonDomAccessException in case of an unexpected Exception during the build.
	 */
	void notifyEndDocument() {
		if(context.hasSchemaStack()) {
			if(context.getSchemaStack().getSchemaContext().hasValidationErrors()) {
				document.setValidationResult(SchemaValidationStatus.FAILED, context.getSchemaStack().getSchemaContext().getValidationExceptions());
			} else {
				document.setValidationResult(SchemaValidationStatus.VALID, null);
			}
		} else {
			document.setValidationResult(SchemaValidationStatus.NOT_VALIDATED, null);
		}
		
		if(callback != null) {
			try {
				callback.onDocument(document);
			} catch(JsonException e) {
				throw e;
			}
		}
	}


	public JsonDocument getDocument() {
		return document;
	}

}
