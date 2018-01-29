package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;

class SchemaBuilder {

	private final BuilderContext context;
	
	SchemaBuilder(BuilderContext context) {
		this.context = context;
	}

	ContainerType build(JsonDocument schemaDoc) {
		JsonObject root = schemaDoc.getRootObject();
		context.setSchemaVersion(root.getString(SchemaConstants.SCHEMA_URI_NAME));
		context.setRootId(root.getString(SchemaConstants.SCHEMA_ID));
		String typeName = root.getString(SchemaConstants.TYPE_NAME);
		SchemaType[] type = context.getTypeFactory(root).createPossibleTypes(root);
		switch(typeName) {
			case SchemaConstants.OBJECT_TYPE: {
				ObjectType rootType = (ObjectType)type[0];
				checkSchemaRootId(rootType);
				context.registerSchemaDefinition(rootType.getId(), type[0]);
				break;
			}
		}
		
		if(type[0] instanceof ContainerType) {
			return (ContainerType)type[0];
		}
		
		throw new JsonSchemaException("Illegal JSON Schema root type '"+type[0].getName()+"'. Expected: " + SchemaConstants.OBJECT_TYPE);
	}

	private void checkSchemaRootId(ObjectType rootType) {
		if(rootType.hasId() && rootType.getId().indexOf(SchemaConstants.HASH)==0) {
			throw new JsonSchemaException("The root schema ID must not be a fragment!");
		}
		
	}

}
