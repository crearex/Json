package ch.crearex.json.schema.builder;

import java.net.URL;
import java.util.LinkedList;

import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.schema.ObjectValidator;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.TypeFactory;

public class BuilderContext {

	private final TypeFactory typeFactory;
	private final TypeFactory enumFactory;
	private final TypeFactory constFactory;
	private final TypeFactory anyTypeFactory;
	private final URL jsonSchemaOriginUrl;
	private final SchemaTypeMap schemaTypeMap;
	private String schemaVersion;
	private String rootId;
	// private ObjectType rootSchema;
	
	private LinkedList<ObjectType> builderStack = new LinkedList<ObjectType>();
	
	public BuilderContext(URL jsonSchemaOriginUrl, SchemaTypeMap schemaTypeMap) {
		this.jsonSchemaOriginUrl = jsonSchemaOriginUrl;
		this.schemaTypeMap = schemaTypeMap;
		this.typeFactory = new SchemaTypeFactory(this);
		this.enumFactory = new EnumTypeFactory(this);
		this.constFactory = new ConstTypeFactory(this);
		this.anyTypeFactory = new AnyTypeFactory(this);
	}
	
	public SchemaTypeMap getSchemaTypeMap() {
		return schemaTypeMap;
	}
	
	public URL getOriginUrl() {
		return jsonSchemaOriginUrl;
	}
	
	public TypeFactory getTypeFactory(JsonObject schemaDefinition) {
		if(schemaDefinition.hasProperty(SchemaConstants.TYPE_NAME) ||
		   schemaDefinition.hasProperty(SchemaConstants.INTERNAL_REFERENCE) ||
		   schemaDefinition.hasProperty(SchemaConstants.ALL_OF)) {
			return typeFactory;
		} else if(schemaDefinition.hasProperty(SchemaConstants.ENUM_NAME)) {
			return enumFactory;
		} else if(schemaDefinition.hasProperty(SchemaConstants.CONST_NAME)) {
			return constFactory;
		} else {
			return anyTypeFactory;
		}
		// throw new JsonSchemaException("Create type factory failed! Missing type definition: type, enum, const.");
	}

	public String getSchemaVersion() {
		return schemaVersion;
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
	
	public ObjectValidator popSchemaDefinition() {
		return builderStack.removeFirst();
	}

	public SchemaType getSchemaDefinition(String fullQualifiedSchemaId) {
		return schemaTypeMap.getSchemaDefinition(fullQualifiedSchemaId);
	}
	
	public SchemaType tryGetSchemaDefinition(String fullQualifiedSchemaId) {
		return schemaTypeMap.tryGetSchemaDefinition(fullQualifiedSchemaId);
	}
	

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	public String getRootId() {
		return this.rootId;
	}

	

}
