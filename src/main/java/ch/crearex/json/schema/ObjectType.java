package ch.crearex.json.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ch.crearex.json.dom.JsonObject;

public class ObjectType extends ContainerType {
	
	private HashMap<String, SchemaType[]> properties = new HashMap<String, SchemaType[]>();
	private HashSet<String> requiredPropertyNames = new HashSet<String>();
	
	public ObjectType(String title, String description) {
		super(title, description);
	}
	
	public void addProperty(String propertyName, SchemaType[] type) {
		properties.put(propertyName, type);
	}
	
	@Override
	public String toString() {
		String retVal = "Object";
		String title = getTitle();
		String description = getDescription();
		if(!title.isEmpty() || !description.isEmpty()) {
			retVal += ":";
		}
		if(!title.isEmpty()) {
			retVal += " " + title + ".";
		}
		if(!description.isEmpty()) {
			retVal += " " + description + ".";
		}
		return retVal;
	}
	
	public SchemaType[] getPropertyType(String name) {
		SchemaType[] type = properties.get(name);
		return type;
	}

	public void addRequiredProperty(String propertyName) {
		requiredPropertyNames.add(propertyName);
	}

	public Set<String> getRequiredPropertyNames() {
		return Collections.unmodifiableSet(requiredPropertyNames);
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return type == JsonObject.class;
	}

}
