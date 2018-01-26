package ch.crearex.json.schema;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

class BuilderContext {

	private final TypeFactory typeFactory;
	private final URL jsonSchemaOriginUrl;
	private final SchemaTypeMap schemaTypeMap;
	private String schemaVersion;
	private String rootId;
	// private ObjectType rootSchema;
	private String rootSchemaId;
	
	private LinkedList<ObjectType> builderStack = new LinkedList<ObjectType>();
	
	BuilderContext(URL jsonSchemaOriginUrl, SchemaTypeMap schemaTypeMap) {
		this.jsonSchemaOriginUrl = jsonSchemaOriginUrl;
		this.schemaTypeMap = schemaTypeMap;
		this.typeFactory = new TypeFactory(this);
	}
	
	SchemaTypeMap getSchemaTypeMap() {
		return schemaTypeMap;
	}
	
	URL getOriginUrl() {
		return jsonSchemaOriginUrl;
	}
	
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public void registerSchemaDefinition(String fullQualifiedSchemaId, SchemaType type) {
		schemaTypeMap.registerSchemaDefinition(fullQualifiedSchemaId, type);
	}

	public void pushSchemaDefinition(ObjectType type) {
		builderStack.addFirst(type);
	}
	
	public ObjectType popSchemaDefinition() {
		return builderStack.removeFirst();
	}

	public SchemaType getSchemaDefinition(String fullQualifiedSchemaId) {
		return schemaTypeMap.getSchemaDefinition(fullQualifiedSchemaId);
	}
	
	public SchemaType tryGetSchemaDefinition(String fullQualifiedSchemaId) {
		return schemaTypeMap.tryGetSchemaDefinition(fullQualifiedSchemaId);
	}
	

	void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	public String getRootId() {
		return this.rootId;
	}

	

}
