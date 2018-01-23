package ch.crearex.json.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.crearex.json.dom.JsonObject;

public class ObjectType extends ContainerType {
	
	private HashMap<String, SchemaType[]> properties = new HashMap<String, SchemaType[]>();
	private HashSet<String> requiredPropertyNames = new HashSet<String>();
	
	private final String id;
	
	public ObjectType(String title, String description, String id) {
		super(title, description);
		
		if(id!=null && id.trim().isEmpty()) {
			throw new JsonSchemaException("The ID of a Object must not be an empty string!");
		}
		this.id = id;
	}
	
	@Override
	public String getName() {
		return SchemaConstants.OBJECT_TYPE;
	}
	
	public void addProperty(String propertyName, SchemaType[] types) {
		properties.put(propertyName, types);
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
	
	public SchemaType[] getPropertyTypes(String name) {
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
	
	public String getId() {
		return this.id;
	}
	
	public boolean hasId() {
		return id != null;
	}
	
	public boolean hasReferenceId() {
		return id != null && id.indexOf(SchemaConstants.HASH) == 0;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
		visitor.visit(this);
		for(Map.Entry<String, SchemaType[]> entry: this.properties.entrySet()) {
			for(SchemaType schemaType: entry.getValue()) {
				if(schemaType instanceof ContainerType) {
					((ContainerType)schemaType).visit(visitor);
				}
			}
		}
	}

}
