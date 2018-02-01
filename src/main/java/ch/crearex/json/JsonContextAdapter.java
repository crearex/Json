package ch.crearex.json;

/**
 * Adapter for extending a JsonContext.
 * Forwards per default every call to the adapted context.
 * 
 * @author Markus Niedermann
 *
 */
public class JsonContextAdapter implements JsonContext {
	
	private JsonContext adaptedContext;
	
	@Override
	public String toString() {
		return adaptedContext.toString();
	}
	
	public JsonContextAdapter(JsonContext adaptedContext) {
		this.adaptedContext = adaptedContext;
	}
	
	public void setAdaptedContext(JsonContext adaptedContext) {
		this.adaptedContext = adaptedContext;
	}

	@Override
	public int getLevel() {
		return adaptedContext.getLevel();
	}

	@Override
	public boolean isResolveStrings() {
		return adaptedContext.isResolveStrings();
	}

	@Override
	public JsonContext setResolveStrings(boolean resolveStrings) {
		adaptedContext.setResolveStrings(resolveStrings);
		return this;
	}

	@Override
	public JsonPath getPath() {
		return adaptedContext.getPath();
	}

	@Override
	public JsonContext setValueFactory(JsonValueFactory valueFactory) {
		adaptedContext.setValueFactory(valueFactory);
		return this;
	}

	@Override
	public JsonValueFactory getValueFactory() {
		return adaptedContext.getValueFactory();
	}

	@Override
	public void validateStructure() {
		adaptedContext.validateStructure();
	}

	@Override
	public JsonContext setJsonCallback(JsonCallback callback) {
		adaptedContext.setJsonCallback(callback);
		return this;
	}

	@Override
	public JsonCallback getJsonCallback() {
		return adaptedContext.getJsonCallback();
	}

}
