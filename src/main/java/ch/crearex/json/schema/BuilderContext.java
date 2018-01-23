package ch.crearex.json.schema;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

class BuilderContext {

	private final TypeFactory typeFactory;
	private final URL jsonSchemaOriginUrl;
	private String schemaVersion;
	private String rootId;
	// private ObjectType rootSchema;
	private String rootSchemaId;
	
	private LinkedList<ObjectType> builderStack = new LinkedList<ObjectType>();
	private HashMap<String, SchemaType> subschemas = new HashMap<String, SchemaType>();
	
	BuilderContext(URL jsonSchemaOriginUrl) {
		this.jsonSchemaOriginUrl = jsonSchemaOriginUrl;
		this.typeFactory = new TypeFactory(this);
	}
	
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public void registerSchemaDefinition(String expandedSubschemaId, SchemaType type) {
//		if(rootSchema==null) {
//			setRootSchemaDefinition(type);
//			return;
//		}

		if(subschemas.containsKey(expandedSubschemaId)) {
			throw new JsonSchemaException("Schema ID '"+expandedSubschemaId+"' already defined!");
		}
		subschemas.put(expandedSubschemaId, type);
	}
	
//	void setRootSchemaDefinition(ObjectType type) {
//		rootSchema = type;
//		rootSchemaId = type.getId();
//		subschemas.put(rootSchemaId, type);
//	}

//	String expandSchemaId(String id) {
//		if(builderStack.size() == 0) {
//			return id;
//		}
//		Iterator<ObjectType> decendingItr = builderStack.descendingIterator();
//		while(decendingItr.hasNext()) {
//			ObjectType next = decendingItr.next();
//			if(next.hasId()) {
//				String parentId = next.getId();
//				if(!isFragment(parentId)) {
//					return parentId + id;
//				}
//			}
//		}
//		throw new JsonSchemaException("Expand Schema ID  '"+id+"' failed! Root not found.");
//	}

	private boolean isFragment(String id) {
		return (id == null) ||  ((id.length() > 1) && (id.charAt(0) == SchemaConstants.HASH));
	}

	public void pushSchemaDefinition(ObjectType type) {
		builderStack.addFirst(type);
	}
	
	public ObjectType popSchemaDefinition() {
		return builderStack.removeFirst();
	}

	public SchemaType getSchemaDefinitionForSubschemaId(String schemaId) {
		SchemaType schema = subschemas.get(schemaId);
		if(schema == null) {
			throw new JsonSchemaException("Resolve schema for '"+schemaId +"' failed! Subschema undefined.");
		}
		return schema;
	}

	void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	public String getRootId() {
		return this.rootId;
	}
	

}
