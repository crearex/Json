package ch.crearex.json;

/**
 * Entry in a {@link JsonPath}.
 * @author Markus Niedermann
 *
 */
public class JsonPathEntry {
	
	private final Integer arrayIndex;
	private final String propertyName;
	private final boolean isRoot;
	
	private JsonPathEntry(String propertyName, Integer arrayIndex, boolean isRoot) {
		this.propertyName = propertyName;
		this.arrayIndex = arrayIndex;
		this.isRoot = isRoot;
	}
	
	/**
	 * Returns true if the entry is the root entry (there is no array index and no property name).
	 */
	public boolean isRoot() {
		return isRoot;
	}
	
	@Override
	public String toString() {
		if(propertyName != null) {
			return propertyName;
		} else if((arrayIndex!=null) && (arrayIndex >= 0)) {
			return arrayIndex.toString();
		} else {
			return "";
		}
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Integer getArrayIndex() {
		return arrayIndex;
	}
	
	public boolean isPropertyNameEntry() {
		return propertyName != null;
	}
	
	public boolean isArrayIndexEntry() {
		return arrayIndex != null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonPathEntry)) {
            return false;
		}
		JsonPathEntry comp = (JsonPathEntry) obj;
		if(comp.arrayIndex != null) {
			return comp.arrayIndex.equals(arrayIndex);
		} else if(comp.propertyName != null) {
			return comp.propertyName.equals(propertyName);
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		if(arrayIndex != null) {
			return 31 * arrayIndex.hashCode();
		} else {
			return 71 * propertyName.hashCode();
		}
	}

	public static JsonPathEntry createEmptyEntry(boolean isRoot) {
		return new JsonPathEntry(null, null, isRoot);
	}

	public static JsonPathEntry createArrayEntry(int index) {
		return new JsonPathEntry(null, index, false);
	}

	public static JsonPathEntry createObjectEntry(String entry) {
		return new JsonPathEntry(entry, null, false);
	}
	
	public static JsonPathEntry createArrayEntry(int index, boolean isRoot) {
		return new JsonPathEntry(null, index, isRoot);
	}

	public static JsonPathEntry createObjectEntry(String entry, boolean isRoot) {
		return new JsonPathEntry(entry, null, isRoot);
	}

	public boolean hasData() {
		return arrayIndex!=null || propertyName!=null;
	}

}
