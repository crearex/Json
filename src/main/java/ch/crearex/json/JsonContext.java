package ch.crearex.json;

/**
 * Contextual object used during the whole parse process.
 * @author Markus Niedermann
 *
 */
public interface JsonContext {
	
	/**
	 * true
	 */
	boolean RESOLVE_STRINGS_DEFAULT = true;
	
	/**
	 * Returns the containment level beginning at level 1 for the
	 * first read object or array.
	 */
	int getLevel();

	/**
	 * Return True if escaped characters are resolved by {@link JsonSimpleValue#asString()}.
	 */
	boolean isResolveStrings();

	/**
	 * @param resolveStrings Set to True if escaped characters shall be resolved by {@link JsonSimpleValue#asString()} (Default: True).
	 */
	JsonContext setResolveStrings(boolean resolveStrings);
	
	/**
	 * Returns the actual {@link JsonPath}.
	 */
	JsonPath getPath();
	
	JsonContext setValueFactory(JsonValueFactory valueFactory);
	JsonValueFactory getValueFactory();
	void validateStructure();

	JsonContext setJsonCallback(JsonCallback callback);

	JsonCallback getJsonCallback();
}
