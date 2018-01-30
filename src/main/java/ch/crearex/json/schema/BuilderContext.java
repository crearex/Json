package ch.crearex.json.schema;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ch.crearex.json.dom.JsonObject;

class BuilderContext {

	private final TypeFactory typeFactory;
	private final TypeFactory enumFactory;
	private final TypeFactory constFactory;
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
		this.typeFactory = new SchemaTypeFactory(this);
		this.enumFactory = new EnumTypeFactory(this);
		this.constFactory = new ConstTypeFactory(this);
	}
	
	SchemaTypeMap getSchemaTypeMap() {
		return schemaTypeMap;
	}
	
	URL getOriginUrl() {
		return jsonSchemaOriginUrl;
	}
	
	public TypeFactory getTypeFactory(JsonObject schemaDefinition) {
		if(schemaDefinition.hasProperty(SchemaConstants.TYPE_NAME) ||
		   schemaDefinition.hasProperty(SchemaConstants.INTERNAL_REFERENCE)) {
			return typeFactory;
		} else if(schemaDefinition.hasProperty(SchemaConstants.ENUM_NAME)) {
			return enumFactory;
		} else if(schemaDefinition.hasProperty(SchemaConstants.CONST_NAME)) {
			return constFactory;
		}
		throw new JsonSchemaException("Create type factory failed! Missing type definition: type, enum, const.");
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
