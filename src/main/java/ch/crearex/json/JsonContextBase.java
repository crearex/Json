package ch.crearex.json;

import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.SchemaStack;

/**
 * Baseclass for <b>all</b> Context implementations.
 * @author Markus Niedermann
 *
 */
public abstract class JsonContextBase implements JsonContext {

	private JsonCallback callback;
	private JsonCallback schema;
	private JsonSchemaCallback schemaCallback = createDefaultSchemaCallback();
	
	/**
	 * If true, do decode JSON-Encoded Strings (e.g. a \n code will be replaced to is character)
	 */
	private boolean resolveStrings = RESOLVE_STRINGS_DEFAULT;
	
	/**
	 * The root container of the actual path to the actual container
	 */
	private Container rootContainer = null;
	
	/**
	 * The actual container values are read from.
	 */
	private Container actualContainer = null;
	
	private JsonValueFactory valueFactory = null;
	
	private int curlyBraceCount = 0;
	private int squareBraceCount = 0;
	
	private SchemaStack schemaStack = null;
		
	/**
	 * POJO abstract linked list entry.
	 * @author Markus Niedermann
	 */
	private static abstract class Container {
		private Container prev = null;
		private Container next = null;
		protected boolean done = false;
		void clear() {
			prev = null;
			if(next != null) {
				next.clear();
			}
			next = null;
		}
	    void setPropertyName(String propertyName) {
		}
		void incIndex() {	
		}
		public abstract void addToPath(JsonPath path);
		public void flagAsDone() {
			done = true;
		}
		public abstract boolean hasData();
		
		Container removePrev() {
			Container ret = prev;
			if(prev!=null) {
				prev.next = null;
			}
			prev = null;
			return ret;
		}
		void setNext(Container next) {
			this.next = next;
		}
		void setPrev(Container prev) {
			this.prev = prev;
		}
		public boolean hasNext() {
			return next != null;
		}
		public Container getNext() {
			return next;
		}
	}
	
	/**
	 * POJO linked list array entry.
	 * @author Markus Niedermann
	 *
	 */
	private static class ArrayContainer extends Container {
		protected int index = -1;
		
		ArrayContainer() {
		}
		void incIndex() {
			index++;
		}
		@Override
		public void addToPath(JsonPath path) {
			if(index>=0 && !done) {
				path.add(new IndexPathEntry(index));
			}
		}
		
		@Override
		public boolean hasData() {
			return index >= 0;
		}
	}
	
	/**
	 * POJO linked list object entry
	 * @author Markus Niedermann
	 */
	private static class ObjectContainer extends Container {
		private String propertyName = null;
		ObjectContainer() {
		}
		void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
		@Override
		public void addToPath(JsonPath path) {
			if(propertyName != null && !done) {
				path.add(new PropertyPathEntry(propertyName));
			}
		}
		
		@Override
		public boolean hasData() {
			return propertyName != null;
		}
	}
	
	public JsonContextBase(JsonCallback callback) {
		setJsonCallback(callback);
	}
	
	@Override
	public JsonContext setJsonCallback(JsonCallback callback) {
		if(callback == null) {
			throw new JsonBuilderException("The JSON CAllback must not be null!");
		}
		this.callback = callback;
		return this;
	}
	
	@Override
	public JsonCallback getJsonCallback() {
		return this.callback;
	}

	@Override
	public JsonContextBase setResolveStrings(boolean resolveStrings) {
		this.resolveStrings = resolveStrings;
		return this;
	}
	
	@Override
	public boolean isResolveStrings() {
		return resolveStrings;
	}
	
	/**
	 * Notify a { to the callback.
	 */
	public void notifyBeginObject() {
		curlyBraceCount++;
		if(actualContainer!=null) {
			actualContainer.incIndex();
		}
		addContainer(new ObjectContainer());
		if(schema!=null) {
			schema.beginObject(this);
		}
		callback.beginObject(this);
	}
	
	/**
	 * notify a } to the callback.
	 */
	public void notifyEndObject() {
		curlyBraceCount--;
		if(!(actualContainer instanceof ObjectContainer)) {
			throw new JsonIllegalSyntaxException("Illegal Syntax: Closing square bracket does not match the opening curly bracket!")
				.setPath(getPath());
		}
		flagContainerAsDone();
		if(schema!=null) {
			schema.endObject(this);
		}
		callback.endObject(this);
		removeLastContainer();
	}
	
	/**
	 * Notify a [ to the callback.
	 */
	public void notifyBeginArray() {
		squareBraceCount++;
		if(actualContainer!=null) {
			actualContainer.incIndex();
		}
		addContainer(new ArrayContainer());
		if(schema!=null) {
			schema.beginArray(this);
		}
		callback.beginArray(this);
	}
	
	/**
	 * Notify a ] to the callback.
	 */
	public void notifyEndArray() {
		squareBraceCount--;
		if(!(actualContainer instanceof ArrayContainer)) {
			throw new JsonIllegalSyntaxException("Illegal Syntax: Closing curly bracket does not match the opening square bracket!")
				.setPath(getPath());
		}
		flagContainerAsDone();
		if(schema!=null) {
			schema.endArray(this);
		}
		callback.endArray(this);
		removeLastContainer();
	}

	
	/**
	 * Returns the path to the actual parser location.
	 */
	@Override
	public JsonPath getPath() {
		JsonPath path = new JsonPath();
		path.add(new RootPathEntry());
		Container item = rootContainer;
		if(item == null) {
			return path;
		}
		
		item.addToPath(path);
		while(item.hasNext()) {
			item = item.getNext();
			item.addToPath(path);
		}
		return path;
	}
	
	/**
	 * Add a container to the POJO-List.
	 * @param container
	 */
	protected void addContainer(Container container) {
		if(rootContainer == null) {
			rootContainer = container;
			actualContainer = container;
		} else {
			actualContainer.setNext(container);
			container.setPrev(actualContainer);
			actualContainer = container;
		}
	}
	
	/**
	 * Removes the last container from the POJO-List.
	 */
	protected void removeLastContainer() {
		if(actualContainer == null) {
			return;
		}
		if(actualContainer == rootContainer) {
			rootContainer = null;
		}
		actualContainer = actualContainer.removePrev();
	}
	
	protected void flagContainerAsDone() {
		if(actualContainer == null) {
			return;
		}
		actualContainer.flagAsDone();
	}
	
	/**
	 * Reset the contextual information used for parsing.
	 * {@link #callback}, {@link #resolveStrings} and {@link #valueFactory}
	 * remains unchanged.
	 */
	public void reset() {
		if(rootContainer != null) {
			rootContainer.clear();
			rootContainer = null;
		}
		actualContainer = null;
	}
	
	/**
	 * Notification before the first { or [ of a JSON Document.
	 */
	public void notifyBeginDocument() {
		curlyBraceCount = 0;
		squareBraceCount = 0;
		if(schema!=null) {
			schema.beginDocument(this);
		}
		callback.beginDocument(this);
	}

	/**
	 * Notification after the last } or ] of a JSON Document.
	 */
	public void notifyEndDocument() {
		validateStructure();
		if(schema!=null) {
			schema.endDocument(this);
		}
		callback.endDocument(this);
	}

	public void validateStructure() {
		if(curlyBraceCount != 0) {
			throw new JsonBuilderException("Invalid Structure: Opening and closing {} mismatch!");
		}
		if(squareBraceCount != 0) {
			throw new JsonBuilderException("Invalid Structure: Opening and closing [] mismatch!");
		}
	}

	public void notifyValue(JsonSimpleValue value) {
		actualContainer.incIndex();
		if(schema!=null) {
			schema.simpleValue(this, value);
		}
		callback.simpleValue(this, value);
	}
	
	public void notifyBoolean(boolean value) {
		actualContainer.incIndex();
		JsonSimpleValue booleanValue = valueFactory.createBooleanValue(value);
		if(schema!=null) {
			schema.simpleValue(this, booleanValue);
		}
		callback.simpleValue(this, booleanValue);
	}

	public void notifyString(String value) {
		actualContainer.incIndex();
		JsonSimpleValue stringValue = valueFactory.createStringValue(value);
		if(schema!=null) {
			schema.simpleValue(this, stringValue);
		}
		callback.simpleValue(this, stringValue);
	}

	public void notifyNumber(String value) {
		actualContainer.incIndex();
		JsonSimpleValue numberValue = valueFactory.createNumberValue(value);
		if(schema!=null) {
			schema.simpleValue(this, numberValue);
		}
		callback.simpleValue(this, numberValue);
	}

	public void notifyNull() {
		actualContainer.incIndex();
		JsonSimpleValue nullValue = valueFactory.createNullValue();
		if(schema!=null) {
			schema.simpleValue(this, nullValue);
		}
		callback.simpleValue(this, nullValue);
	}
	
	public void notifyComma() {
		if(schema!=null) {
			schema.comma(this);
		}
		callback.comma(this);
	}

	/**
	 * Notify the property-name (not the value)
	 * @param propertyName
	 */
	public void notifyProperty(String propertyName) {
		actualContainer.setPropertyName(propertyName);
		if(schema!=null) {
			schema.property(this, propertyName);
		}
		callback.property(this, propertyName);
	}

	@Override
	public JsonContext setValueFactory(JsonValueFactory valueFactory) {
		this.valueFactory = valueFactory;
		return this;
	}

	@Override
	public JsonValueFactory getValueFactory() {
		return valueFactory;
	}

	public JsonContextBase setSchema(JsonSchema jsonSchema) {
		this.schema = jsonSchema;
		return this;
	}
	
	public JsonContextBase setSchemaCallback(JsonSchemaCallback schemaCallback) {
		if(schemaCallback == null) {
			schemaCallback = createDefaultSchemaCallback();
		}
		this.schemaCallback = schemaCallback;
		return this;
	}
	
	JsonSchemaCallback createDefaultSchemaCallback() {
		return new JsonSchemaCallback() {
			@Override
			public void schemaViolation(JsonSchemaValidationException violation) {
				throw violation;
			}};
	}
	
	public JsonSchemaCallback getSchemaCallback() {
		return schemaCallback;
	}

	public void setSchemaStack(SchemaStack schemaStack) {
		this.schemaStack = schemaStack;
	}
	
	public boolean hasSchemaStack() {
		return schemaStack != null;
	}
	
	public SchemaStack getSchemaStack() {
		return schemaStack;
	}

}
