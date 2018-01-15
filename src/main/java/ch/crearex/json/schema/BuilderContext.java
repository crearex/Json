package ch.crearex.json.schema;

import ch.crearex.json.JsonSchemaCallback;

class BuilderContext {

	private final TypeFactory typeFactory;
	
	private String schemaVersion;
	private ContainerType rootObject;
	
	BuilderContext() {
		this.typeFactory = new TypeFactory(this);
	}
	
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public void setRoot(ContainerType rootObject) {
		this.rootObject = rootObject;
	}

}
